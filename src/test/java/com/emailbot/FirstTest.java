package com.emailbot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class FirstTest extends Assert {

    private static WebDriver webDriver;
    private static String email;
    private static String password;
    private By mailLocator = By.cssSelector("div.b-datalist_search > div.b-datalist__body > div.b-datalist__item");

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Gleb Durygin\\Documents\\IdeaProjects\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver","C:\\Users\\Gleb Durygin\\Documents\\IdeaProjects\\geckodriver.exe");
        ///ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        webDriver = new ChromeDriver();
        //webDriver = new FirefoxDriver();
        webDriver.manage().window().maximize();
        webDriver.get("https://mail.ru/");
        email = "red90label@mail.ru";
        password = "h7k8jbnm";
    }

    @Test
    public void userLogin() {
        WebElement loginField= new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.id("mailbox:login")));
        loginField.sendKeys(email);
        WebElement passwordField = new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.id("mailbox:password")));
        passwordField.sendKeys(password);
        WebElement loginButton = new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.id("mailbox:submit")));
        loginButton.click();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement checkEmail = new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.id("PH_user-email")));

        String mailUser = checkEmail.getTagName();
        assertEquals(mailUser,"i");
    }

    @After
    public void tearDown() {
        webDriver.quit();
    }

}
