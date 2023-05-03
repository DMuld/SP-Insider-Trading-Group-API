package com.backend.springboot;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

public class TestRegister {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "/home/colin/Documents/UNO/SEMESTER 6/CSCI4830/Group Project/SP-Insider-Trading-Group-API/lib/chromedriver");
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }

  @Test
  public void testRegister() throws Exception {
    driver.get("http://34.125.220.174/LoginRegister/login.html");
    Thread.sleep(500);
    driver.findElement(By.linkText("Register")).click();
    Thread.sleep(500);
    driver.get("http://34.125.220.174/LoginRegister/register.html");
    Thread.sleep(500);
    driver.findElement(By.id("newuseremail")).click();
    Thread.sleep(500);
    driver.findElement(By.id("newuseremail")).clear();
    Thread.sleep(500);
    driver.findElement(By.id("newuseremail")).sendKeys("5678");
    Thread.sleep(500);
    driver.findElement(By.id("newuserpassword1")).click();
    Thread.sleep(500);
    driver.findElement(By.id("newuserpassword1")).clear();
    Thread.sleep(500);
    driver.findElement(By.id("newuserpassword1")).sendKeys("5678");
    Thread.sleep(500);
    driver.findElement(By.id("newuserpassword2")).click();
    Thread.sleep(500);
    driver.findElement(By.id("newuserpassword2")).clear();
    Thread.sleep(500);
    driver.findElement(By.id("newuserpassword2")).sendKeys("5678");
    Thread.sleep(500);
    driver.findElement(By.id("registerSubmit")).click();
    Thread.sleep(500);
    driver.get("http://34.125.220.174/LoginRegister/login.html");
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
