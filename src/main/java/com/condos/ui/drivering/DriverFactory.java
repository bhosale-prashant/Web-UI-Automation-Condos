package com.condos.ui.drivering;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	
	WebDriver driver ;
	public Properties prop;
	
	// Load data from properties file to [roperties object
	public Properties init_prop() {
		try {
			FileInputStream ip = new FileInputStream("src\\test\\resources\\Config_and_OR.properties");
			prop = new Properties();
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	// Initialize driver
	public WebDriver init_driver(String browsername) {
		if (browsername.toUpperCase().equals("CHROME")){
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browsername.toUpperCase().equals("IE") || browsername.toUpperCase().equals("INTERNET EXPLORER")){
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		} else if (browsername.toUpperCase().equals("FIREFOX")){
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else {
			System.out.println("Browser name cannot be accepted");
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		return driver;
	}
}