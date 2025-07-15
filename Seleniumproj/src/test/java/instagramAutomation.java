import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class instagramAutomation {
    @Test
    public static void main(String[] args) throws InterruptedException {

        // Setup WebDriver
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            // Step 1: Go to Instagram login page
            driver.get("https://www.instagram.com/accounts/login/");
            Thread.sleep(5000); // Wait for page to load

            // Step 2: Enter credentials
            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            username.sendKeys("testinginsta709");
            password.sendKeys("testing123");

            // Step 3: Click login
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            Thread.sleep(8000); // Wait for homepage

            // Step 4: Handle popups
            try {
                WebElement notNow1 = driver.findElement(By.xpath("//button[text()='Not Now']"));
                notNow1.click();
                Thread.sleep(3000);
                WebElement notNow2 = driver.findElement(By.xpath("//button[text()='Not Now']"));
                notNow2.click();
            } catch (Exception ignored) {
                System.out.println("Popups skipped or not found.");
            }

            // Step 5: Go to specific post
            driver.get("https://www.instagram.com/sashika__dilmina/p/CcegGJnrTfk/");
            Thread.sleep(6000); // Wait for post to load

            // Step 6: Like the post if not already liked
            try {
                // Locate the Like SVG icon with aria-label 'Like'
                WebElement likeButton = driver.findElement(
                        By.xpath("//*[name()='svg' and @aria-label='Like']")
                );

                // Click the like button
                likeButton.click();
                System.out.println("Liked the post.");
            } catch (Exception e) {
                System.out.println("Like action failed: " + e.getMessage());
            }


            // Step 7: Comment on the post
            try {
                WebElement commentIcon = driver.findElement(By.xpath("//*[name()='svg' and @aria-label='Comment']"));
                commentIcon.click();
                Thread.sleep(3000);

                WebElement commentBox = driver.findElement(By.xpath("//textarea[@aria-label='Add a comment…']"));
                commentBox.click();
                commentBox.sendKeys("You're gorgeous...");
                Thread.sleep(2000);

                WebElement postButton = driver.findElement(By.xpath("//div[@role='button' and text()='Post']"));
                postButton.click();
                System.out.println("Comment posted.");
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println("Comment action failed: " + e.getMessage());
            }

            // Step 8: Save the post
            try {
                WebElement saveButton = driver.findElement(By.xpath("//section//span//*[name()='svg' and @aria-label='Save']"));
                saveButton.click();
                System.out.println("Post saved!");
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println("Save action failed: " + e.getMessage());
            }

       } finally {
            // Step 9: Close browser

            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}
