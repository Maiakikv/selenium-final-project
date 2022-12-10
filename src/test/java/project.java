import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.util.*;


public class project {
    WebDriver driver;

    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            this.driver = new ChromeDriver();
            this.driver.manage().window().maximize();
        } else if (browser.equalsIgnoreCase("Edge")) {
            WebDriverManager.edgedriver().setup();
            this.driver = new EdgeDriver();
            this.driver.manage().window().maximize();
        } else {
            System.out.println("incorrect browser");
        }

    }

    @Test
    public void swoopTest() {
        //  - Navigate to the swoop.ge
        driver.get("https://www.swoop.ge/");

        // - Go to 'კინო'
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement allMovies = driver.findElement(By.className("cat-0"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.cat-0")));
        allMovies.click();

        //- Select the first movie in the returned list and click on ‘ყიდვა’ button
        WebElement imageToHover = driver.findElement(By.xpath("//div[@class='movie-main-img'][1]"));
        Actions action = new Actions(driver);
        action.moveToElement(imageToHover).build().perform();
        WebElement firstMovie = driver.findElement(By.xpath("//p[text()='ყიდვა'][1]"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='ყიდვა'][1]")));
        firstMovie.click();

        //- Scroll vertically and choose ‘კავეა ისთ ფოინთი’ [ჰორიზონტალურად დასქროლვა და შემდეგ ვერტიკალურად, რომ მივწვდე ისთ ფოინთს]
        WebElement mostRightTheatre = driver.findElement(By.xpath("//ul[contains(@class, 'cinema-tabs ui')]/li[last()]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", mostRightTheatre);
        js.executeScript("window.scrollBy(0, -230)");
        WebElement eastPoint = driver.findElement(By.xpath("//a[text()='კავეა ისთ ფოინთი']/ancestor-or-self::li"));
        eastPoint.click();

        //- Check that only ‘კავეა ისთ ფოინთი’ options are returned
        List<WebElement> movieOptions = driver.findElements(By.xpath("//div[contains(@class,'ui')][@aria-hidden='false']/div/div[@aria-hidden='false']/a/p[@class='cinema-title']"));
        for (WebElement element : movieOptions
        ) {
            Assert.assertEquals(element.getText(), "კავეა ისთ ფოინთი");
        }

        // - Click on last date and then click on last option
        WebElement lastDAte = driver.findElement(By.xpath("//div[@id='384933']//ul//li[last()]"));
        lastDAte.click();
        List<WebElement> options = driver.findElements(By.cssSelector("div.seanse-details[aria-hidden='false'][style=\"display: flex;\"]"));
        WebElement lastOption = options.get(options.size() - 1);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        wait.until(ExpectedConditions.elementToBeClickable(lastOption));
        lastOption.click();

        // - Check in opened popup that movie name, cinema and datetime is valid
        String popUpText = (((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;", new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-header']")))).toString());
        String movieTitle = driver.findElement(By.className("movie-title")).getText();
        String cinemaName = driver.findElement(By.xpath("//p[@class='movie-cinema'][text()='კავეა ისთ ფოინთი']")).getText();
        String dateTime = driver.findElement(By.xpath("//p[@class='movie-cinema'][last()]")).getText();

        Assert.assertTrue(popUpText.contains(movieTitle));
        Assert.assertTrue(popUpText.contains(cinemaName));
        Assert.assertTrue(popUpText.contains(dateTime));

        //  - Choose any vacant place
        List<WebElement> freeSeats = driver.findElements(By.xpath("//div[@class='seat free']/div[@class='seat-new-part']"));
        if (freeSeats.get(0).isEnabled()) {
            freeSeats.get(0).click();
        } else
            freeSeats.get(0).click();
            System.out.println("All the seats are taken");

        //- Register for a new account
        WebElement registerButton = driver.findElement(By.xpath("//p[@class='register']"));
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();

        //- Choose ‘იურიდიული პირი’
        WebElement legalEntity = driver.findElement(By.xpath("//li[contains(@class, 'juridial')]"));
        legalEntity.click();

        //- Fill all mandatory with not valid data and optional fields, in case of dropdowns choose any non-default option
        List<WebElement> allElements = driver.findElements(By.xpath("//div[contains(@class, 'register-content-2')]"));
        wait.until(ExpectedConditions.visibilityOfAllElements(allElements));

        Select dropLegalForm = new Select(driver.findElement(By.xpath("//select[@name='LegalForm']")));
        dropLegalForm.selectByIndex(2);
        Select countryCode = new Select(driver.findElement(By.xpath("//select[@name='CountryCode']")));
        countryCode.selectByIndex(3);
        Select contactPersonGender = new Select(driver.findElement(By.xpath("//select[@name='ContactPersonGender']")));
        contactPersonGender.selectByVisibleText("ქალი");
        Select contactPersonCountryCode = new Select(driver.findElement(By.xpath("//select[@name='ContactPersonCountryCode']")));
        contactPersonCountryCode.selectByValue("AF");
        List <WebElement> fields =driver.findElements(By.xpath("//div[contains(@class, 'register-content-2')]//input[@class='mail-input']"));
        for (WebElement field: fields
             ) {
            field.sendKeys("777");
        }

        WebElement checkOfTermsAndConditions = driver.findElement(By.id("IsLegalAgreedTerms"));
        if (!checkOfTermsAndConditions.isSelected())
            checkOfTermsAndConditions.click();
        WebElement submit = driver.findElement(By.xpath("//a[@onclick='SubmitLegalForm()']"));
        submit.click();

        // - Check that error message ‘რეგისტრაციის დროს დაფიქსირდა შეცდომა!’ is appear
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[text()='რეგისტრაციის დროს დაფიქსირდა შეცდომა!']")));
        WebElement errorMessage = driver.findElement(By.xpath("//p[text()='რეგისტრაციის დროს დაფიქსირდა შეცდომა!']"));
        js.executeScript("arguments[0].scrollIntoView();", errorMessage);
        String messageText= errorMessage.getText();
        Assert.assertEquals(messageText, "რეგისტრაციის დროს დაფიქსირდა შეცდომა!" );


    }
    @AfterMethod
  public void after() {
       this.driver.quit();
    }
}