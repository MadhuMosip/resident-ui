package io.mosip.testrig.residentui.utility;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import io.mosip.testrig.residentui.fw.util.AdminTestUtil;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.kernel.util.KeycloakUserManager;
import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.testcase.*;


public class TestRunner {
	static TestListenerAdapter tla = new TestListenerAdapter();

	public static String jarUrl = TestRunner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	public static String uin="";
	public static String perpetualVid="";
	public static String onetimeuseVid="";
	public static String temporaryVid="";

	static TestNG testNg;

	public static void main(String[] args) throws Exception {
		AdminTestUtil.initialize();
		String identityGenManual=JsonUtil.JsonObjParsing(Commons.getTestData(),"identityGenManual");
		if(identityGenManual.equals("yes")) {
			uin=JsonUtil.JsonObjParsing(Commons.getTestData(),"UIN");
			perpetualVid=JsonUtil.JsonObjParsing(Commons.getTestData(),"perpetualvid");
			onetimeuseVid=JsonUtil.JsonObjParsing(Commons.getTestData(),"onetimevid");
			temporaryVid=JsonUtil.JsonObjParsing(Commons.getTestData(),"temporaryvid");
		}else {
			uin = AdminTestUtil.generateUIN();

			if (uin != null) {
				perpetualVid = AdminTestUtil.generateVID(uin, "perpetual");
				onetimeuseVid = AdminTestUtil.generateVID(uin, "onetimeuse");
				temporaryVid= AdminTestUtil.generateVID(uin, "temporary");
			}
		}

		startTestRunner();
	}



	public static void startTestRunner() throws Exception {
		testNg=new TestNG();
		testNg.setDefaultSuiteName("resident");
		MockSMTPListener mockSMTPListener = new MockSMTPListener();
		mockSMTPListener.run();

		String listExcludedGroups=JsonUtil.JsonObjParsing(Commons.getTestData(),"setExcludedGroups");
		testNg.setExcludedGroups(listExcludedGroups);		
		testNg.setTestClasses(new Class[] {
				LoginTest.class,    
				ViewMyHistory.class,
				ManageMyVid.class,
				SecureMyId.class,
				UpdateMyData.class,
				TrackMyRequests.class,
				GetPersonalisedCard.class,
				ShareMyData.class,
				GetMyUIN.class,
				GetInformation.class,
				VerifyPhoneNumberEmailID.class

		});
		String langid=JsonUtil.JsonObjParsing(Commons.getTestData(),"language");
		String language=JsonUtil.JsonObjParsing(Commons.getTestData(),"loginlang");
		
			if(language.equals("sin")) {
				langid="";
			}
		
		System.getProperties().setProperty("testng.outpur.dir", "testng-report");
		testNg.setOutputDirectory("testng-report");
		System.getProperties().setProperty("emailable.report2.name", "ResidentUI" + "-"
				+ System.getProperty("env.user")+ "-"+langid+-+ System.currentTimeMillis() + "-report.html");


		testNg.run();
		System.exit(0);
	}

public static String getGlobalResourcePath() {
		if (checkRunType().equalsIgnoreCase("JAR")) {
			return new File(jarUrl).getParentFile().getAbsolutePath().toString();
		} else if (checkRunType().equalsIgnoreCase("IDE")) {
			String path = new File(TestRunner.class.getClassLoader().getResource("").getPath()).getAbsolutePath()
					.toString();
			if (path.contains("test-classes"))
				path = path.replace("test-classes", "classes");
			return path;
		}
		return "Global Resource File Path Not Found";
	}

	public static String getResourcePath() {
		if (checkRunType().equalsIgnoreCase("JAR")) {
			return new File(jarUrl).getParentFile().getAbsolutePath().toString()+"/resources/";
		} else if (checkRunType().equalsIgnoreCase("IDE")) {
			String path = System.getProperty("user.dir") +  System.getProperty("path.config");

			//	String path = new File(TestRunner.class.getClassLoader().getResource("").getPath()).getAbsolutePath()
			//				.toString();
			if (path.contains("test-classes"))
				path = path.replace("test-classes", "classes");
			return path;
		}
		return "Global Resource File Path Not Found";
	}

	public static String checkRunType() {
		if (TestRunner.class.getResource("TestRunner.class").getPath().toString().contains(".jar"))
			return "JAR";
		else
			return "IDE";
	}
	public static String GetKernalFilename(){
		String path = System.getProperty("env.user");
		String kernalpath=null;
		if(System.getProperty("env.user")==null) {
			kernalpath="Kernel.properties";

		}else {
			kernalpath="Kernel_"+path+".properties";
		}
		return kernalpath;
	}

}
