import { Component, OnInit, OnDestroy } from "@angular/core";
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from "@ngx-translate/core";
import { Subscription } from "rxjs";
import { Router } from "@angular/router";
import Utils from 'src/app/app.utils';
import { AppConfigService } from 'src/app/app-config.service';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { MatDialog } from '@angular/material';
import { InteractionService } from "src/app/core/services/interaction.service";
import { ThrowStmt } from "@angular/compiler";
import { HostListener } from '@angular/core';
import {saveAs} from 'file-saver'

@Component({
  selector: "app-revokevid",
  templateUrl: "revokevid.component.html",
  styleUrls: ["revokevid.component.css"],
})
export class RevokevidComponent implements OnInit, OnDestroy {
  langJSON: any;
  popupMessages: any;
  subscriptions: Subscription[] = [];
  selectedValue: string = "generatevid";
  vidlist: any;
  policyType: any;
  vidType: string = "";
  notificationType: Array<string> = [];
  vidValue: string = "";
  finalTypeList = {};
  clickEventSubscription: Subscription;
  newVidType: any;
  message: string;
  newVidValue:string;
  rowHeight:string = "2:1.2";
  cols:number = 4;
  showInfoCard:boolean = false;
  iIconVidType:any;
  infoText:any;
  eventId:any;

  constructor(private interactionService: InteractionService, private dialog: MatDialog, private appConfigService: AppConfigService, private dataStorageService: DataStorageService, private translateService: TranslateService, private router: Router) {
    this.clickEventSubscription = this.interactionService.getClickEvent().subscribe((id) => {
      if (id === "confirmBtn") {
        this.generateVID(this.newVidType)
      }else if (id === "deleteVID"){
        this.revokeVID(this.newVidValue)
      }else if(id === "downloadVID"){
        this.vidDownloadStatus(this.newVidValue)
      }
    })
  }

  async ngOnInit() {
    this.translateService.use(localStorage.getItem("langCode"));
    this.cols = (window.innerWidth <= 1400) ? 3 : 4 
    this.rowHeight = (window.innerWidth <= 1420) ? "2:1.3" : "2:1.2"
   

    this.translateService
      .getTranslation(localStorage.getItem("langCode"))
      .subscribe(response => {
        this.langJSON = response;
        this.popupMessages = response;
      });
    this.getVID();
  }

  getVID() {
    this.dataStorageService
      .getVIDs()
      .subscribe((response) => {
        if (response["response"])
          this.vidlist = response["response"];
        this.getPolicy();
      });
  }

  getPolicy() {
    let self = this;
    let results = [];
    self.finalTypeList = {};
    this.dataStorageService.getPolicy().subscribe(response => {
      if (response["response"]) {
        this.policyType = JSON.parse(response["response"]);
        for (var i = 0; i < this.policyType.vidPolicies.length; i++) {
          results = [];
          for (var j = 0; j < self.vidlist.length; j++) {
            if (self.vidlist[j].vidType.toUpperCase() === this.policyType.vidPolicies[i].vidType.toUpperCase()) {
              self.vidlist[j].showvid = false;
              results.push(self.vidlist[j]);
            }
          }
          // console.log("this.policyType.vidPolicies[i].vidType>>>"+this.policyType.vidPolicies[i].vidType);
          self.finalTypeList[this.policyType.vidPolicies[i].vidType] = results;
          console.log(this.finalTypeList)
          console.log(this.finalTypeList)
        }
      }
    },
      error => {
        console.log(error);
      }
    );
  }

  onResize(event:any){
    this.cols = (event.target.innerWidth  <= 1400 ) ? 3 : 4
    this.rowHeight = (event.target.innerWidth <= 1430) ? "2:1.3" : "2:1.2"
  }

  displayVid(finalTypeList, policyType, policy, showvid) {
    console.log(policyType)
    let self = this;
    let results = [];
    for (var j = 0; j < self.vidlist.length; j++) {
      if (self.vidlist[j].vidType.toUpperCase() === policyType.toUpperCase()) {
        if (self.vidlist[j].vid === policy.vid) {
          self.vidlist[j].showvid = showvid;
        } else {
          self.vidlist[j].showvid = false;
        }
        results.push(self.vidlist[j]);
      }
    }
    self.finalTypeList[policyType] = results;
  }

  setvidType(event: any) {
    this.vidType = "";
    this.vidType = event.value;
  }

  sendNotification(event: any) {
    if (!this.notificationType.includes(event.source.value)) {
      this.notificationType.push(event.source.value);
    } else {
      this.notificationType.forEach((item, index) => {
        if (item === event.source.value) this.notificationType.splice(index, 1);
      });
    }
  }

  generateVIDBtn(vidType: any) {
    this.newVidType = vidType
    this.showWarningMessage(vidType)
  }

  generateVID(vidType: any) {
    let self = this;
    const request = {
      "id": this.appConfigService.getConfig()["resident.vid.id.generate"],
      "version": this.appConfigService.getConfig()["resident.vid.version.new"],
      "requesttime": Utils.getCurrentDate(),
      "request": {
        "transactionID": (Math.floor(Math.random() * 9000000000) + 1).toString(),
        "vidType": vidType,
        "channels": ["PHONE", "EMAIL"]
      }
    };
    this.dataStorageService.generateVID(request).subscribe(response => {
      this.message = this.popupMessages.genericmessage.manageMyVidMessages.createdSuccessfully.replace("$vidType",this.newVidType)
      this.eventId = response.headers.get("eventId")
      if (!response.body["errors"].length) {
        setTimeout(() => {
          self.getVID();
        }, 400);
        this.showMessage(this.message.replace("$eventId", this.eventId ),this.eventId);
      } else {
        this.showErrorPopup(response.body["errors"][0].errorCode);
      }
    });
  }

  downloadVIDBtn(vid:any,vidType:any){
    this.showDownloadMessage(vidType)
    this.newVidValue = vid
    this.newVidType = vidType
  }

  vidDownloadStatus(vid:any){
      this.dataStorageService.vidDownloadStatus(vid).subscribe(response =>{
        if(!response.body['errors'].length){
          setTimeout(()=>{
            this.downloadVidCard(response.headers.get("eventid"))
          },120000)
        }else{
          console.log("error>>"+response.body['errors'])
        }
      },
      error =>{
        console.log(error)
      })
  }

  downloadVidCard(eventId:any){
     this.dataStorageService.downloadVidCardStatus(eventId).subscribe(response =>{
      this.eventId = response.headers.get("eventid")
      this.message = this.popupMessages.genericmessage.manageMyVidMessages.downloadedSuccessFully.replace("$eventId", this.eventId).replace("$vidType",this.newVidType)
      let fileName = ""
      const contentDisposition = response.headers.get('Content-Disposition');
      console.log(contentDisposition)
      if (contentDisposition) {
        const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        const matches = fileNameRegex.exec(contentDisposition);
        if (matches != null && matches[1]) {
          fileName = matches[1].replace(/['"]/g, '');
        }
      }
      saveAs(response.body, fileName);
      this.showMessage(this.message, this.eventId)
     })
  }

  

  revokeVIDBtn(vidValue: any,vidType:any){
    this.showDeleteMessage(vidType)
    this.newVidValue = vidValue
    this.newVidType - vidType
  }

  revokeVID(vidValue: any) {
    let self = this;
    const request = {
      "id": this.appConfigService.getConfig()["mosip.resident.revokevid.id"],
      "version": this.appConfigService.getConfig()["resident.revokevid.version.new"],
      "requesttime": Utils.getCurrentDate(),
      "request": {
        "transactionID": (Math.floor(Math.random() * 9000000000) + 1).toString(),
        "vidStatus": "REVOKED"
      }
    };
    this.dataStorageService.revokeVID(request, vidValue).subscribe(response => {
      console.log(response.headers.get("eventid"))
      this.eventId = response.headers.get("eventid")
      this.message = this.popupMessages.genericmessage.manageMyVidMessages.deletedSuccessfully.replace("$eventId", this.eventId).replace("$vidType",this.newVidType)
      if (!response.body["errors"].length) {
        setTimeout(() => {
          self.getVID();
          // this.showMessage(this.message ,vidValue);
        }, 400);
        this.showMessage(this.message ,this.eventId);
      } else {
        this.showErrorPopup(response.body["errors"][0].errorCode);
      }
    },
      error => {
        console.log(error);
      }
    );
  }

  showMessage(message: string,eventId:string) {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.successLabel,
        eventId:eventId,
        message: message,
        clickHere:this.popupMessages.genericmessage.clickHere,
        btnTxt: this.popupMessages.genericmessage.successButton
      }
    });
    return dialogRef;
  }

  showDeleteMessage(vidType: string) {
    this.message = this.popupMessages.genericmessage.manageMyVidMessages[vidType].confirmationMessageForDeleteVid
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.warningLabel,
        btnTxtNo: this.popupMessages.genericmessage.noButton,
        message: this.message,
        btnTxt: this.popupMessages.genericmessage.deleteButton
      }
    });
    return dialogRef;
  }

  showDownloadMessage(vidType: string) {
    this.message = this.popupMessages.genericmessage.manageMyVidMessages[vidType].confirmationMessageForDownloadVid 
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.warningLabel,
        btnTxtNo: this.popupMessages.genericmessage.noButton,
        message: this.message,

        btnTxt: this.popupMessages.genericmessage.downloadLabel
      }
    });
    return dialogRef;
  }
  


  showWarningMessage(vidType: any) {
    if (vidType === "Perpetual") {
      if (this.finalTypeList["Perpetual"].length) {
        this.message = this.popupMessages.genericmessage.manageMyVidMessages[vidType].WarningMessageLabel
      } else {
        this.message = this.popupMessages.genericmessage.manageMyVidMessages[vidType].confirmationMessageForCreateVid
      }
    }else{
      this.message = this.popupMessages.genericmessage.manageMyVidMessages[vidType].confirmationMessageForCreateVid
    }

    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.warningLabel,
        message: this.message,
        btnTxt: this.popupMessages.genericmessage.yesButton,
        btnTxtNo: this.popupMessages.genericmessage.noButton
      }
    });
    return dialogRef;
  }

  showErrorPopup(message: string) {
    this.message = this.popupMessages.serverErrors[message]
    this.dialog
      .open(DialogComponent, {
        width: '550px',
        data: {
          case: 'MESSAGE',
          title: this.popupMessages.genericmessage.errorLabel,
          message: this.message,
          btnTxt: this.popupMessages.genericmessage.successButton
        },
        disableClose: true
      });
  }

  onToggle(event: any) {
    this.selectedValue = event.source.value;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  onItemSelected(item: any) {
    this.router.navigate([item]);
  }

  openPopupMsg(vidType:any){
    this.showInfoCard = true
    this.iIconVidType = vidType
    this.infoText =this.popupMessages.InfomationContent.revokevid[vidType]
  }

}
