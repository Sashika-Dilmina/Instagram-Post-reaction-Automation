import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class InstagramAutomation {

    @Test
    public static void main(String[] args) {


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // 1. Open Instagram
            driver.get("https://www.instagram.com/");

            // 2. Login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("testinginsta709");
            driver.findElement(By.name("password")).sendKeys("testing123");
            driver.findElement(By.xpath("//button[@type='submit']")).click();

            // 3. Handle "Save Your Login Info?" pop-up (optional)
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(text(), 'Not Now')]"))).click();

            // 4. Handle "Turn on Notifications" pop-up (optional)
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(text(), 'Not Now')]"))).click();

            // 5. Navigate to a post or hashtag
            driver.get("https://www.instagram.com/p/POST_ID/"); // Replace POST_ID

            // 6. Like the post
            WebElement likeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//section//span//*[name()='svg' and @aria-label='Like']")));
            likeButton.click();

            System.out.println("Post liked successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Keep browser open for inspection or close it after a delay
            // driver.quit();
        }
    }
}
