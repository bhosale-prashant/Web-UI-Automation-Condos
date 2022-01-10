package com.condos.ui.functions;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.condos.ui.common.TestNeeds;

public class ThingsOnScreen extends TestNeeds {

	WebDriver driver;
	public Properties prop;

	public ThingsOnScreen (WebDriver driver) {	
		this.driver= driver;	
	}

	// Wait to check visibility of a locator, within the given timeout
	public void checkVisiblityOfElement (By locator, int timeout ) {

		WebDriverWait wait = new WebDriverWait (driver,timeout);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));	
	}

	// Wait and click a locator after its clickable, within the given timeout
	public void clickElementWhenReady (By locator, int timeout ) {

		WebDriverWait wait = new WebDriverWait (driver,timeout);
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();	
	}

	// With few retries, try to tackle stale element click issue
	public void handleStaleElementClickIssue (By locator) {

		boolean staleElement = true;
		int attempts = 0;
		while (staleElement && attempts < 10) {
			try{
				driver.findElement(locator).click();
				staleElement = false;
				break;
			} catch (StaleElementReferenceException e) {
				staleElement = true;
			}
			attempts++;
		}
	}

	// Get page title
	public String getPageTitle() {	
		return driver.getTitle();
	}

	// Login using data from properties file and verify successful login
	public boolean loginHere (Properties prop) {
		By LogInButton = By.xpath(prop.getProperty("locator.logIn.option.button"));
		checkVisiblityOfElement (LogInButton,10);
		handleStaleElementClickIssue (LogInButton);

		By LogInEmail = By.xpath(prop.getProperty("locator.logIn.email"));
		checkVisiblityOfElement (LogInEmail,10);

		boolean isEmailInputVisible = driver.findElement(LogInEmail).isDisplayed();
		if (!isEmailInputVisible) {
			driver.navigate().refresh();
			checkVisiblityOfElement (LogInButton,10);
			handleStaleElementClickIssue (LogInButton);
			checkVisiblityOfElement (LogInEmail,10);
		}

		driver.findElement(LogInEmail).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath(prop.getProperty("locator.logIn.password"))).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath(prop.getProperty("locator.logIn.submit.button"))).click();

		By MyAccountMenu = By.xpath(prop.getProperty("locator.myAccount.menu"));
		checkVisiblityOfElement (MyAccountMenu,10);
		WebElement myAccountMenu = driver.findElement(MyAccountMenu);
		Actions actions = new Actions(driver);
		actions.moveToElement(myAccountMenu).perform();

		boolean loginFlag = false;
		if (driver.findElements(By.xpath(prop.getProperty("locator.logOut.menu"))).size() == 0) {
			loginFlag = false;
		} else {
			loginFlag = true;
		}
		return loginFlag;
	}

	// Search for location given in properties file 
	public void searchLocationHere (Properties prop) {

		By filterSearchBegin = By.xpath(prop.getProperty("locator.filterSearch.begin"));
		checkVisiblityOfElement (filterSearchBegin,10);		
		driver.findElement(filterSearchBegin).click();

		By filterSearchText = By.xpath(prop.getProperty("locator.filterSearch.text"));
		checkVisiblityOfElement (filterSearchText,10);

		driver.findElement(filterSearchText).sendKeys(prop.getProperty("location"));
		driver.findElement(By.xpath(prop.getProperty("locator.filterSearch.selectThis"))).click();
	}

	// Sort search results as configured in properties file and wait until search results reload completely
	public void sortButtonSearchResultsHere (Properties prop) {

		// Click Sort option
		By sortButtonSearchResults = By.xpath(prop.getProperty("locator.sort.button.searchResults"));
		checkVisiblityOfElement (sortButtonSearchResults,10);
		clickElementWhenReady (sortButtonSearchResults,10);

		// Select the kind of sort
		By sortOptionSearchResults = By.xpath(prop.getProperty("locator.sort.option.searchResults"));
		checkVisiblityOfElement (sortOptionSearchResults,10);
		clickElementWhenReady (sortOptionSearchResults,10);

		// Wait until the search results are displayed according to the desired sort
		URL aURL;
		try {
			aURL = new URL(driver.getCurrentUrl());
			String pageNumberWithFilter = "//li[contains(@class,'PaginationItem')]/a[contains(@href,'"+aURL.getPath()+"?"+aURL.getQuery()+"') and .='1']";
			checkVisiblityOfElement (By.xpath(pageNumberWithFilter),10);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	// Select the house property with locator as indexed in the properties file
	public void selectPropertyHere (Properties prop) {

		By selectProperty = By.xpath(prop.getProperty("locator.searchResults.selectProperty"));
		checkVisiblityOfElement (selectProperty,10);
		handleStaleElementClickIssue (selectProperty);
	}

	// Write preview data to a json file
	public void writePreviewToJsonHere (Properties prop) throws FileNotFoundException {

		JSONObject jObjPreview = new JSONObject();
		jObjPreview.put("AskingPrice", driver.findElement(By.xpath(prop.getProperty("locator.preview.value.AskingPrice"))).getText());
		jObjPreview.put("Address", driver.findElement(By.xpath(prop.getProperty("locator.preview.value.Address"))).getText());
		jObjPreview.put("InfoHolder", driver.findElement(By.xpath(prop.getProperty("locator.preview.value.InfoHolder"))).getText());
		jObjPreview.put("Maintainance Fee", driver.findElement(By.xpath(prop.getProperty("locator.preview.value.MaintainanceFee"))).getText());
		jObjPreview.put("Mls", driver.findElement(By.xpath(prop.getProperty("locator.preview.value.Mls"))).getText());
		jObjPreview.put("Brokerage", driver.findElement(By.xpath(prop.getProperty("locator.preview.value.Brokerage"))).getText());

		//Writing data from preview page to json file
		PrintWriter pWriter = new PrintWriter("DetailsFromPreviewPage.json");
		pWriter.write(jObjPreview.toJSONString());

		String dirPreviewJson = System.getProperty("user.dir");
		System.out.println("===============================================");
		System.out.println("DetailsFromPreviewPage.json is stored inside "+dirPreviewJson);

		pWriter.flush();
		pWriter.close();
	}

	// Write data frpm details page, to a json file
	public void writeDetailsToJsonHere (Properties prop) throws FileNotFoundException {

		WebDriverWait wait = new WebDriverWait (driver,10);
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		for(String winHandle : driver.getWindowHandles()){
			driver.switchTo().window(winHandle);
		}

		JSONObject jObjDetailView = new JSONObject();

		By listingAge = By.xpath(prop.getProperty("locator.details.value.ListingAge"));

		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		checkVisiblityOfElement (listingAge,10);

		jObjDetailView.put("Listing Age", driver.findElement(listingAge).getText());
		jObjDetailView.put("Apt name and number", driver.findElement(By.xpath(prop.getProperty("locator.details.value.AptNameAndNumber"))).getText());

		String titleText =  driver.findElement(By.xpath(prop.getProperty("locator.details.value.Title"))).getText();
		titleText = titleText.split("-")[1];
		titleText = titleText.trim();

		String filename = titleText.substring(titleText.indexOf(' '),titleText.length());
		filename = filename.trim();

		jObjDetailView.put("Building", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Building"))).getText());

		List <WebElement> addressList = driver.findElements(By.xpath(prop.getProperty("locator.details.value.AddressInline.list")));
		String completeAddress = "";    
		for ( WebElement eachAddressLine: addressList) {
			completeAddress = completeAddress+eachAddressLine.getText()+", ";
		}
		completeAddress = completeAddress.substring(0, completeAddress.length() - 2);
		jObjDetailView.put("Address", completeAddress);

		jObjDetailView.put("Bed", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Bed"))).getText());
		jObjDetailView.put("Bath", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Bath"))).getText());
		jObjDetailView.put("Parking", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Parking"))).getText());
		jObjDetailView.put("Tax", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Tax"))).getText());
		jObjDetailView.put("Actual size", driver.findElement(By.xpath(prop.getProperty("locator.details.value.ActualSize"))).getText());
		jObjDetailView.put("Maintenance fees", driver.findElement(By.xpath(prop.getProperty("locator.details.value.MaintenanceFees"))).getText());
		jObjDetailView.put("Age of building", driver.findElement(By.xpath(prop.getProperty("locator.details.value.AgeOfBuilding"))).getText());
		jObjDetailView.put("Price per sq ft", driver.findElement(By.xpath(prop.getProperty("locator.details.value.PricePerSqFt"))).getText());
		jObjDetailView.put("Exposure", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Exposure"))).getText());
		jObjDetailView.put("Possession", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Possession"))).getText());
		jObjDetailView.put("Outdoor space", driver.findElement(By.xpath(prop.getProperty("locator.details.value.OutdoorSpace"))).getText());
		jObjDetailView.put("Locker", driver.findElement(By.xpath(prop.getProperty("locator.details.value.Locker"))).getText());

		//Writing data from details page to json file
		PrintWriter pWriter = new PrintWriter(filename+".json");
		pWriter.write(jObjDetailView.toJSONString());

		String dirDetailsJson = System.getProperty("user.dir");
		System.out.println(filename+".json is stored inside "+dirDetailsJson);
		System.out.println("===============================================");

		pWriter.flush();
		pWriter.close();
	}
}
