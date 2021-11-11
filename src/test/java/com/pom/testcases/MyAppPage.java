package com.pom.testcases;

import java.util.Hashtable;


import org.testng.annotations.Test;

import com.pom.base.Page;
import com.pom.utilities.TestUtil;

public class MyAppPage extends Page {

	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp1")
	public void registration(Hashtable<String, String> data) {

		type("userID_XPATH", data.get("username"));
		type("emailID_XPATH", data.get("email"));
		type("passID_XPATH", data.get("password"));

		click("signUP_XPATH");

		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
