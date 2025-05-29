import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class KiwiDuckTest extends ConfigTest {

    @BeforeMethod
    public void initConfig() {
        ConfigTest.initializeProfileDirectory();
        ChromeOptions options = ConfigTest.createChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testKiwiDuckSite() {

        driver.get("https://kiwiduck.github.io");

        //переходим к странице Select
        driver.findElement(By.linkText("Selenium practice (elements)")).click();
        driver.findElement(By.xpath("//a[@href='select']")).click();

        //На странице “Select” выбрать одно значение в выпадающем списке
        WebElement heroElement = driver.findElement(By.name("hero"));
        Select heroSelect = new Select(heroElement);
        heroSelect.selectByVisibleText("Linus Torvalds");

        //и несколько в списке, поддерживающем множественный выбор
        WebElement languagesElement = driver.findElement(By.name("languages"));
        Select languagesSelect = new Select(languagesElement);
        if (languagesSelect.isMultiple()) {
            languagesSelect.selectByValue("Java");
            languagesSelect.selectByValue("C#");
        }

        //Нажать на кнопку “GET RESULT”
        driver.findElement(By.xpath("//button[@id='go' and text()='Get Results']")).click();

        //Проверить, что на странице отобразились выбранные значения
        Assert.assertEquals(driver.findElements(By.xpath("//label[@name='result']")).get(0)
                .getText(), heroSelect.getFirstSelectedOption().getText());
        List<WebElement> elements = languagesSelect.getAllSelectedOptions();
        StringBuilder factString = new StringBuilder();
        for (WebElement we : elements) {
            factString.append(we.getDomAttribute("value")).append(", ");
        }
        String expectedString = driver.findElements(By.xpath("//label[@name='result']")).get(1)
                .getText();
        Assert.assertEquals(factString.toString().trim().substring(0, factString.toString().trim().length() - 1),
                expectedString.trim());

        //и ссылка с текстом 'Great! Return to menu' и нажать на нее
        driver.findElement(By.xpath("//a[@href and text()='Great! Return to menu']")).click();

        //переходим к странице Form
        driver.findElement(By.linkText("Selenium practice (elements)")).click();
        driver.findElement(By.xpath("//a[@href='form']")).click();

        //На странице 'Form' заполнить все обязательные поля
        driver.findElement(By.xpath("//label[text()='First Name:']/following-sibling::input"))
                .sendKeys("Даутов");
        driver.findElement(By.xpath("//label[text()='Last Name:']/following-sibling::input"))
                .sendKeys("Марсель");
        driver.findElement(By.xpath("//label[text()='Email:']/following-sibling::input"))
                .sendKeys("mymail@cbr.ru");
        String sex = "Male";
        driver.findElement(By.xpath("//label[text()='Sex:']/parent::div//text()[contains(.," +
                        Quotes.escape(sex) + ")]/preceding-sibling::input"))
                .click();
        driver.findElement(By.xpath("//label[text()='Address:']/following-sibling::input"))
                .sendKeys("Москва");
        driver.findElement(By.xpath("//label[text()='Avatar:']/following-sibling::input"))
                .sendKeys(System.getProperty("user.dir") + "/src/test/resources/avatar.tif");
        driver.findElement(By.xpath("//label[text()='Tell me something about yourself']/following-sibling::textarea"))
                .sendKeys("Центральный Банк");

        //и нажать на кнопку «ОТПРАВИТЬ»
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        //Проверить, что появилась ссылка с текстом 'Great! Return to menu'
        Assert.assertTrue(driver.findElements(By.xpath("//a[@href and text()='Great! Return to menu']")).size() > 0,
                "ссылка с текстом 'Great! Return to menu' не отображается");

        //и нажать на нее
        driver.findElement(By.xpath("//a[@href and text()='Great! Return to menu']")).click();

        //переходим к странице IFrame
        driver.findElement(By.linkText("Selenium practice (elements)")).click();
        driver.findElement(By.xpath("//a[@href='iframe']")).click();

        driver.switchTo().frame("code-frame");
        WebElement codeElement = driver.findElement(By.xpath("//*[@id='code']"));
        String codeText = codeElement.getText().replaceAll(".*?([a-z0-9]+)$", "$1");
        driver.switchTo().defaultContent();
        WebElement input = driver.findElement(By.xpath("//input[@name='code']"));
        input.sendKeys(codeText);
        WebElement button = driver.findElement(By.xpath("//input[@value='Verify']"));
        button.click();
        Assert.assertTrue(driver.findElements(By.xpath("//a[@href and text()='Great! Return to menu']")).size() > 0,
                "ссылка с текстом 'Great! Return to menu' не отображается");
        driver.findElement(By.xpath("//a[@href and text()='Great! Return to menu']")).click();
    }
}
