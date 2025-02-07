package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class login_page {
    WebDriver driver;
    @Test(dataProvider = "data")
    public void login(String username, String password){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys(username);
        WebElement passwordsend= driver.findElement(By.xpath("//input[@type=\"password\"]"));
        passwordsend.sendKeys(password);
        WebElement subbmit = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
        subbmit.click();




}

@DataProvider(name = "data")
public Object[][] dataset1() {
    return new Object[][]
            {
                    {"Admin", "admin123"}

            };

}
    public WebDriver getDriver() {
        return driver;
    }

}
