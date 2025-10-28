import { Component } from '@angular/core';
import { AppConfigService } from './app-config.service';
import { Router } from '@angular/router';
import defaultJson from "src/assets/i18n/default.json";

// Minimal shell only

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [],
})

export class AppComponent {
  title = 'resident-ui';
  primaryLangCode: string = localStorage.getItem("langCode");
  sitealignment;

  constructor(
    private appConfigService: AppConfigService,
    private router: Router,
  ) {
    this.appConfigService.getConfig();
    if (this.primaryLangCode === "ara") {
      localStorage.setItem('direction','rtl')
    }else{
      localStorage.setItem('direction','ltr')
    }
    const sitealignment = localStorage.getItem('direction');
    document.body.dir = sitealignment;
  }
  
  ngOnInit() { 
    if(!localStorage.getItem("selectedfontsize")){
      localStorage.setItem("selectedfontsize", "14");
    }
    // Minimal shell
  }
}
