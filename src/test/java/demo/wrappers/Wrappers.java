package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    public static void clickOnElement(ChromeDriver driver,WebElement element){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(30));
        try{
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            System.out.println("User clicked on element");
        }catch(Exception e){
            System.out.println("Exception occurred while clicking on element :"+e.getMessage());
            e.getStackTrace();

        }
    }
}
