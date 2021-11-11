package com.pom.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.testng.annotations.DataProvider;

import com.pom.base.Page;

//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;



import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class TestUtil extends Page {

	public static String sspath;
	public static String ssName;

//	public static void captureScreenShot() throws IOException {
//		Date d = new Date();
//		ssName = d.toString().replace(":","-").replace(" ", "_")+".jpg";
//		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//		FileUtils.copyFile(scrFile, new File (System.getProperty("user.dir"+"\\target\\surefire-reports\\html\\"+ssName)));
//	}
//	
	public static void CaptureSS() throws IOException {

		Date d = new Date();
		ssName = d.toString().replace(":", "-").replace(" ", "_").concat(".jpg");
		System.out.println(ssName);

		Screenshot capture = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver);
		BufferedImage img = capture.getImage();
		ImageIO.write(img, "jpg", new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + ssName));

	}

	@DataProvider(name = "dp1")
	public Object[][] getData(Method m) {

		String sheetName = m.getName();
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);

		Object[][] data = new Object[rows -1 ][1];

		Hashtable<String, String> table = null;

		for (int rowNum = 2; rowNum <= rows; rowNum++) {

			table = new Hashtable<String, String>();
			for (int colNum = 0; colNum < cols; colNum++) {
				table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
				data[rowNum - 2][0] = table;
			}
		}
		return data;
	}

	public static boolean isTestRunnable(String testName, ExcelReader excel) {
		String Sname = "test_suite";
		int rows = excel.getRowCount(Sname);

		for (int rNum = 2; rNum <= rows; rNum++) {

			String testcase = excel.getCellData(Sname, "TCID", rNum);

			if (testcase.equalsIgnoreCase(testName)) {
				String runmode = excel.getCellData(Sname, "runmode", rNum);

				if (runmode.equalsIgnoreCase("Y")) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;

	}

}
