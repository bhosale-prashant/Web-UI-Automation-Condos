# Web-UI-Automation-Condos
Sample Web UI automation project using Selenium Webdriver with Java and testng.

So the task is:
1. Go to the URL (mentioned inside the project's properties file)
2. Input location and select the same from area dropdown
3. You will get a list of results on that page.
4. Select the 5th result on that list (it’s dynamic) and go to its details.
5. Store these details on a json file and set the filename with one of the values from the above details.

Dependencies required to setup and run this project are mentioned in [ProjectPath]\pom.xml file.

Replace the Username and Password with correct values, inside the following properties file:
[ProjectPath]\src\test\resources\Config_and_OR.properties

Once the setup is fine for you, try running the following java file as TestNG Test:
[ProjectPath]\src\test\java\com\condos\ui\scenarios\ScenariosOnScreen.java
