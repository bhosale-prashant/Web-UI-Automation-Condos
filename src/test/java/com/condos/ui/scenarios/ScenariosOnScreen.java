package com.condos.ui.scenarios;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.condos.ui.common.TestNeeds;

public class ScenariosOnScreen  extends TestNeeds {

	@Test (priority = 0)
	public void pageTitleTest(){
		String page_title= ui.getPageTitle();
		Assert.assertEquals("Condos.ca | Search & Analyze all Toronto Condos | Buy Sell Rent Invest", page_title);
	}

	@Test (priority = 1)
	public void login(){
		boolean loginStatus = ui.loginHere(prop);
		Assert.assertTrue(loginStatus);
	}
	
	@Test (dependsOnMethods = { "login" })
	public void searchLocation(){
		ui.searchLocationHere(prop);
	}
	
	@Test (dependsOnMethods = { "searchLocation" })
	public void sortButtonSearchResults(){
		ui.sortButtonSearchResultsHere(prop);
	}
	
	@Test (dependsOnMethods = { "sortButtonSearchResults" })
	public void writePreviewToJson() throws FileNotFoundException {
		ui.writePreviewToJsonHere(prop);
	}
	
	@Test (dependsOnMethods = { "writePreviewToJson" })
	public void selectProperty(){
		ui.selectPropertyHere(prop);
	}
	
	@Test (dependsOnMethods = { "selectProperty" })
	public void writeDetailsToJson() throws FileNotFoundException {
		ui.writeDetailsToJsonHere(prop);
	}
}
