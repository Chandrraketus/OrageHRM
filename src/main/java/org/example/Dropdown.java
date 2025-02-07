package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Dropdown {
    WebDriver driver;

    @Test(dataProvider = "data")
    public void login(String username, String password) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));


        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();


        WebElement userprofile = driver.findElement(By.xpath("//img[@alt='profile picture']"));
        userprofile.click();


        List<WebElement> menuItems = driver.findElements(By.xpath("//ul[@class='oxd-dropdown-menu']//li"));
        System.out.println("Total items found: " + menuItems.size());

        for (WebElement item : menuItems) {
            String itemText = item.getText().trim();
            System.out.println("Found item: " + itemText);

            if (itemText.equals("Support")) {

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.elementToBeClickable(item)).click();


                try {
                    item.click();
                } catch (Exception e) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", item);
                }
                break;
            }
        }
    }

    @DataProvider(name = "data")
    public Object[][] dataset1() {
        return new Object[][]{{"Admin", "admin123"}};
    }

    public WebDriver getDriver() {
        return driver;
    }
}
