import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Test5 {

    public static void main(String[] args) throws IOException {
        // Set up Chrome driver
        WebDriverManager.chromedriver().setup();

        // Create ChromeOptions for incognito mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito -–disable-notifications");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        // Create WebDriver instance
        WebDriver driver = new ChromeDriver(options);

        //Set Timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(3));

        // Open the URL
        driver.get("https://www.naturesbasket.co.in/");
        driver.manage().window().maximize();

        // Close the pop-up window if any

       try{ driver.findElement(By.id("btnClosePopUpBox")).click(); }
       catch(Exception e){
           System.out.println(e.getMessage());
       }

        // Select the state Kolkata
        Select select=new Select(driver.findElement(By.id("ctl00_ddlCitySearch")));
        select.selectByValue("kolkata");

        // Close popups if any
        try{ driver.findElement(By.id("btnClosePopUpBox")).click(); }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        // Mouse hover over the chocolates dropdown and click on Chocolates using Actions class
        Actions actions = new Actions(driver);
        WebElement masterSearch= driver.findElement(By.id("ctl00_txtMasterSearch1"));
        WebElement subSearch= driver.findElement(By.xpath("//ul[@id='ctl00_NonPanIndia']//a[contains(text(),'Chocolates')]"));
        actions.moveToElement(masterSearch).moveToElement(subSearch).click().build().perform();

        // Add two items of chocolates to the cart using JavaScriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement order1=driver.findElement(By.xpath("//div[contains(@class,'pro-id_7358')]//div//div[contains(@class,'search_AddCart1')]"));
        js.executeScript("arguments[0].click()",order1);
        WebElement addPin= driver.findElement(By.xpath("//input[@id='btnAddPin']"));
        js.executeScript("document.querySelector(\"#txt\").value='700001'");
        js.executeScript("arguments[0].click();",addPin);
        WebElement addOk= driver.findElement(By.xpath("//*[@id=\"btnAddPin\" and @value='OK']"));
        js.executeScript("arguments[0].click();",addOk);
        WebElement order2=driver.findElement(By.xpath("//div[contains(@class,'pro-id_1236')]//div//div[contains(@class,'search_AddCart1')]"));
        js.executeScript("arguments[0].click();",order2);

        // Click on any of the visible chocolates and go to the detailed view
         driver.findElement(By.xpath("//img[@title='Ferrero Rocher Gift Pack 200G (16 Pc)']")).click();

        // Get the name of the chocolate heading
        String chocolateName = driver.findElement(By.xpath("//h1[text()=\"Ferrero Rocher Gift Pack 200G (16 Pc)\"]")).getText();

        // Go back to the previous page
        driver.navigate().back();

        // Scroll to the last part of the current page (possible once as it will load infinitely) and close any popup that comes if present.
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try{ driver.findElement(By.id("btnClosePopUpBox")).click(); }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        // Enter dummy email ID
        WebElement emailField=driver.findElement(By.xpath("//input[@id=\"ctl00_txtNewletter\"]"));
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(emailField));
        emailField.sendKeys("dummyemailid@example.com");


        //Clear + paste chocolate name to the email field
        actions.keyDown(Keys.CONTROL).sendKeys("a").sendKeys(Keys.BACK_SPACE).keyUp(Keys.CONTROL).sendKeys(chocolateName).build().perform();
        js.executeScript("arguments[0].scrollIntoView(true);", emailField);


        // Take screenshot
        String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        String screenshotName = chocolateName + "_" + timestamp + ".png";
        TakesScreenshot screenshot=(TakesScreenshot) driver;
        File srcF = screenshot.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcF,new File ("./Screenshot/"+screenshotName+".png"));

        // Close the browser window
        driver.quit();
    }
}
