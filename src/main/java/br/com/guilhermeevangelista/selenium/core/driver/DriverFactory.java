package br.com.guilhermeevangelista.selenium.core.driver;

import br.com.guilhermeevangelista.selenium.core.driver.enums.Web;
import br.com.guilhermeevangelista.selenium.core.utils.PropertiesManager;
import br.com.guilhermeevangelista.selenium.core.utils.variaveis.VariaveisEstaticas;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Classe com design pattern singleton para trabalhar com instancias do Driver
 *
 * @author Guilherme-Evangelista
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>() {
        @Override
        protected synchronized WebDriver initialValue() {
            return initDriver();
        }
    };

    protected static WebDriverWait wait;

    /**
     * Recuperar instancia unica do Web Driver
     *
     * @return retorna o WebDriver
     */
    public static WebDriver getDriver() {
        return threadDriver.get();
    }

    /**
     * Recuperar instancia unica do Web Driver Wait
     *
     * @return retorna o WebDriverWait
     */
    public static WebDriverWait getDriverWait() {
        return wait;
    }

    /**
     * Criar driver com o design pattern singleton
     */
    private static WebDriver initDriver() {
        WebDriver driver = Web.CHROME.getDriver();

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            driver.manage().window().maximize();
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            driver.manage().window().fullscreen();
        }
        wait = new WebDriverWait(driver, 10);
        PropertiesManager propertiesManager = new PropertiesManager("config");
        driver.get(propertiesManager.getProp("url" + VariaveisEstaticas.ambiente));
        return driver;
    }

    /**
     * Fecha e navegador
     */
    public static void closeDriver() {
        WebDriver driver = getDriver();
        if(driver != null) {
            driver.quit();
        }
        threadDriver.remove();
    }

    /**
     * Mata tarefa do Driver
     */
    public static void killDriver() {
        WebDriver driver = getDriver();
        if(driver != null) {
            driver.quit();
        }
        threadDriver.remove();
    }

}
