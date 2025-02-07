package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class dashboard implements ITestListener {
    WebDriver driver;
    ExtentTest test;
    ExtentReports extent;

        @BeforeTest
        public void Report(){
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("Test-output/extentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            sparkReporter.config().setDocumentTitle("HRM_orangeloginpage");
            sparkReporter.config().setReportName("Extent Report");
            sparkReporter.config().setTheme(Theme.DARK);
        }
    @Test(dataProvider = "LoginData")
    public void dasdhboard(String username ,String password) throws InterruptedException {
        test = extent.createTest("Login Test - " + username);
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys(username);
        WebElement passwordsend = driver.findElement(By.xpath("//input[@type=\"password\"]"));
        passwordsend.sendKeys(password);
        WebElement subbmit = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
        subbmit.click();

        try {
            WebElement errorMesaage = driver.findElement(By.xpath("//p[@class=\"oxd-text oxd-text--p oxd-alert-content-text\"]"));
            if (errorMesaage.isDisplayed()) {
                System.out.println("Login failed: Invalid credentials detected.");
                test.fail("Login failed: Invalid credentials.");
                Assert.fail("Login failed due to incorrect credentials");
            }
        }
        catch (NoSuchElementException e){
            System.out.println("Login successful or error message not found.");
        }

    List<WebElement> dashboardProduct=driver.findElements(By.xpath("//a"));
        for(WebElement dashboardProducts:dashboardProduct){
         String nameofproduct= dashboardProducts.getText();
            System.out.println(nameofproduct);

            if (nameofproduct.equals("My Info")){
                dashboardProducts.click();


            }
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException {
       Thread.sleep(10);
        if (result.getStatus() == ITestResult.SUCCESS){
            String screenshotpath2 = captureScreenshot(result.getName());
            test.log(Status.PASS,"Test  Passed" +result.getThrowable());
            test.addScreenCaptureFromPath(screenshotpath2);

       if(result.getStatus() == ITestResult.FAILURE){
           String screenshotpath = captureScreenshot(result.getName());
           test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
           test.addScreenCaptureFromPath(screenshotpath);


           }
            driver.quit();
        }
    }

    @DataProvider(name = "LoginData")
    public  Object[][] dataset(){
        return  new Object[][]
                {
                        { "Admin", "admin123"},
                        { "Admin", "admin123111"}

        };
    }

    public String captureScreenshot(String testname){
            File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String destinationPath =  "Test-output/screenshot/" +testname + ".png";
            File destinationfile =new File(destinationPath);
            try {
                FileUtils.copyFile(srcFile,destinationfile);

                } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return destinationPath;
    }


    }

