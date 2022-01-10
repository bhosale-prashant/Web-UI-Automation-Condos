package com.condos.ui.common;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.condos.ui.drivering.DriverFactory;
import com.condos.ui.functions.ThingsOnScreen;

public class TestNeeds {

	WebDriver driver ;
	DriverFactory df;
	public Properties prop;
	public ThingsOnScreen ui;

	@BeforeTest
	public void setup(){
		df = new DriverFactory();
		prop = df.init_prop();
		driver = df.init_driver(prop.getProperty("browser"));
		ui = new ThingsOnScreen (driver);
		driver.get(prop.getProperty("url"));
	}
	
	@AfterTest
	public void teardown(){
		driver.quit();
	}
}
