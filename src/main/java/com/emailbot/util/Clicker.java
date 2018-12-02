package com.emailbot.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Clicker {
    private WebDriver webDriver;
    private String link;

    public Clicker(String link) {
        this.link = link;
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Gleb Durygin\\Documents\\IdeaProjects\\geckodriver.exe");
        webDriver = new FirefoxDriver();
        webDriver.manage().window().maximize();
        webDriver.get(link);
    }

    public boolean clickOnButton() {
        WebElement loginButton = new WebDriverWait(webDriver, 4).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".filter-pp__btn mr10 js-assign-booking loading-after")));
        if (loginButton != null)
        {
            loginButton.click();
            return true;
        } else return false;
    }
}
