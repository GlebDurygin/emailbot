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

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Gleb Durygin\\Documents\\IdeaProjects\\chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.get("https://mail.ru/");
        email = "mail@mail.ru";
        password = "12345678i";
    }

    @Test
    public void userLogin() {
        WebElement loginField= new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.id("mailbox:login")));
        loginField.sendKeys(email);
        WebElement passwordField = new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.id("mailbox:password")));
        passwordField.sendKeys(password);
        WebElement loginButton = new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.id("mailbox:submit")));
        loginButton.click();
        WebElement checkEmail = webDriver.findElement(By.id("PH_user-email"));
        //WebElement checkEmail = webDriver.findElement(By.cssSelector(".PH_user-email"));
        //WebElement checkMail= new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".b-datalists")));
        String mailUser = checkEmail.getTagName();
        System.out.println(webDriver.getCurrentUrl());
        assertEquals(mailUser,"i");
    }

    @After
    public void tearDown() {
        webDriver.quit();
    }

}
