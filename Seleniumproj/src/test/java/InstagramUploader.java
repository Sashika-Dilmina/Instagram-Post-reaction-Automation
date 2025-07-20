import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class InstagramUploader {

    private static final String USERNAME = "pmw234with";
    private static final String PASSWORD = "pmw@234with";
    private static final String IMAGE_PATH = "C:\\Users\\SASHIKA DILMINA\\Downloads\\photo.jpeg";

    @Test
    public static void main(String[] args) {
        // Set path to ChromeDriver executable


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.get("https://www.instagram.com/");
            System.out.println("Instagram opened");

            handleCookieBanner(driver, wait);
            login(driver, wait);
            handlePopups(driver, wait);
            waitForHomePage(driver, wait);
            createAndUploadPost(driver, wait);

            System.out.println("✅ Post uploaded successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            takeScreenshot(driver);
        } finally {
            driver.quit();
        }
    }

    private static void waitForHomePage(WebDriver driver, WebDriverWait wait) throws Exception {
        try {
            WebDriverWait longerWait = new WebDriverWait(driver, Duration.ofSeconds(60));

            longerWait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@placeholder,'Search')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='button']//svg[@aria-label='New post']")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href,'/home')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href,'/create')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//main[@role='main']")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@alt,'profile')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Stories') or @aria-label='Stories']"))
            ));

            System.out.println("✅ Home page fully loaded");
        } catch (Exception e) {
            System.out.println("Current URL during failure: " + driver.getCurrentUrl());
            System.out.println("Page title during failure: " + driver.getTitle());
            throw new Exception("Failed to detect home page loaded: " + e.getMessage());
        }
    }

    private static void handleCookieBanner(WebDriver driver, WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Allow') or contains(text(),'Accept')]"))
            ).click();
            System.out.println("Cookie banner handled");
        } catch (Exception e) {
            System.out.println("No cookie banner found");
        }
    }

    private static void login(WebDriver driver, WebDriverWait wait) throws Exception {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameField.sendKeys(USERNAME);

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys(PASSWORD);

        driver.findElement(By.xpath("//button[@type='submit']")).click();
        System.out.println("Login attempted");

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("accounts/onetap"),
                    ExpectedConditions.urlContains("challenge"),
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@href,'/home') or contains(@aria-label,'Home')]"))
            ));

            if (driver.getCurrentUrl().contains("challenge") || driver.getPageSource().contains("Suspicious Login Attempt")) {
                throw new Exception("Login blocked - security challenge detected");
            }

            System.out.println("Login successful");
        } catch (Exception e) {
            throw new Exception("Login failed: " + e.getMessage());
        }
    }

    private static void handlePopups(WebDriver driver, WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Not Now') or contains(text(), 'Not now')]"))
            ).click();
            System.out.println("'Save info' popup dismissed");
        } catch (Exception ignored) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Not Now') or contains(text(), 'Not now')]"))
            ).click();
            System.out.println("Notifications popup dismissed");
        } catch (Exception ignored) {}
    }

    private static void createAndUploadPost(WebDriver driver, WebDriverWait wait) throws Exception {
        WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div/div/div[2]/div/div/div[1]/div[1]/div[2]/div/div/div/div/div[2]/div[7]/div/span/div/a/div")
        ));
        System.out.println("✅ 'New Post' button located");

        createButton.click();
        System.out.println("📸 Create post button clicked");

        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
        fileInput.sendKeys(IMAGE_PATH);
        System.out.println("📁 Image selected: " + IMAGE_PATH);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(@style, 'background-image') or contains(@src, 'blob')]")
        ));
        System.out.println("🖼 Image upload verified");

        clickNextButton(wait, "First");
        clickNextButton(wait, "Second");

        // ✅ FIXED Share button XPath (text-based, not brittle absolute path)
        WebElement shareButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='Share' or .='Share']")
        ));
        shareButton.click();
        System.out.println("📤 Share button clicked");

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Your post has been shared')]")),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Post shared')]"))
            ));
            System.out.println("✅ Post confirmed as shared");
        } catch (Exception e) {
            System.out.println("⚠ Post may be shared, but confirmation message not found");
        }
    }


    private static void clickNextButton(WebDriverWait wait, String step) throws Exception {
        try {
            // Try common patterns for the "Next" button on Instagram
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(
                            "//div[text()='Next' or span/text()='Next' or button//div/text()='Next']" +
                                    "| //button//div[text()='Next']" +
                                    "| //button[text()='Next']" +
                                    "| //div[@role='button' and text()='Next']"
                    )
            ));

            nextButton.click();
            System.out.println("✅ " + step + " Next button clicked");
        } catch (Exception e) {
            throw new Exception("❌ Failed to click " + step + " Next button", e);
        }
    }


    private static void takeScreenshot(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            System.out.println("🖼 Screenshot captured for debugging");
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}
