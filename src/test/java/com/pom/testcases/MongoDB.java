package com.pom.testcases;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Hashtable;

import org.testng.annotations.Test;


import org.openqa.selenium.support.ui.ExpectedConditions;

import com.pom.base.Page;
import com.pom.utilities.TestUtil;

public class MongoDB extends Page {

	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp1")
	public void deleteUser(Hashtable<String, String> data) {

		jse.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		driver.get(config.getProperty("MongoDBurl"));
		type("login_XPATH", data.get("id"));
		click("nextBtn_XPATH");
		type("passWd_XPATH",data.get("pass"));
		click("nextBtn_XPATH");
		try {
			Thread.sleep(7000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		click("browseCol_XPATH");
		click("userList_XPATH");
		jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		try {
			Thread.sleep(4500L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		action.moveToElement(locator("delete_XPATH")).perform();
		wait.until(ExpectedConditions.visibilityOf(locator("delete_XPATH"))).click();
		wait.until(ExpectedConditions.visibilityOf(locator("delconfirm_XPATH"))).click();
	}
}
