package br.com.guilhermeevangelista.selenium.core.driver.enums;

import br.com.guilhermeevangelista.selenium.core.driver.interfaces.WebApplication;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum que contem valores para inicializacao de diferentes browsers
 *
 * @author Guilherme-Evangelista
 */
public enum Web implements WebApplication {
    CHROME{
        @Override
        public WebDriver getDriver() {
//            if (System.getProperty("os.name").toLowerCase().contains("win")) {
//                System.setProperty("webdriver.chrome.driver", "C:\\Software\\Selenium\\chromedriver.exe");
//            }else if (System.getProperty("os.name").toLowerCase().contains("mac")){
//                System.setProperty("webdriver.chrome.driver", "src/main/resources/propriedades/chromedriver");
//            }
            WebDriverManager.chromedriver().win().setup();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);

            return new ChromeDriver(options);
        }
    },

    FIREFOX{
        @Override
        public WebDriver getDriver() {
//            System.setProperty("webdriver.firefox.driver", "src/main/resources/propriedades/geckodriver.exe");
            WebDriverManager.firefoxdriver().win().setup();
            return new FirefoxDriver();
        }
    }

}
