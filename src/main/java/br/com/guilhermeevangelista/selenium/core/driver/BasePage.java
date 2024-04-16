package br.com.guilhermeevangelista.selenium.core.driver;

import br.com.guilhermeevangelista.selenium.core.utils.report.screenshot.ScenarioRepository;
import freemarker.log.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static br.com.guilhermeevangelista.selenium.core.driver.DriverFactory.getDriver;
import static br.com.guilhermeevangelista.selenium.core.driver.DriverFactory.getDriverWait;

/**
 * Classe a ser extendida pelas pages a fim de utilizar metodos abstraidos do Selenium e inicializar os elementos com o
 * PageFactory initElements
 *
 * @author Guilherme-Evangelista
 */
public class BasePage {

    public BasePage() {
        PageFactory.initElements(getDriver(), this);
    }

    private static final Logger log = Logger.getLogger(BasePage.class.getName());

    /**
     * Metodo para realizar um input de texto em um elemento
     *
     * @param elemento campo mapeado
     * @param texto    texto da a ser digitado
     */
    protected void digitarTexto(WebElement elemento, String texto) {
        try {
            waitProcessPage();
            getDriverWait()
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOf(elemento))
                    .sendKeys(texto);
        } catch (WebDriverException e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value='" + texto + "';", elemento);
        } catch (Exception e) {
            ScenarioRepository.screenShot();
            log.error("Falha ao digitar no elemento: " + elemento, e);
        }
    }

    /**
     * Metodo para clicar em um elemento de 3 formas caso alguma delas falhem
     * 1º - Selenium
     * 2º - Actions
     * 3º - JavaScript
     *
     * @param elemento elemento mapeado nas pages
     */
    protected void clicarElemento(WebElement elemento) {
        try {
            waitProcessPage();
            getDriverWait()
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.elementToBeClickable(elemento))
                    .click();
        } catch (ElementNotInteractableException e) {
            new Actions(getDriver())
                    .moveToElement(elemento)
                    .click()
                    .perform();
        } catch (WebDriverException e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", elemento);
        } catch (Exception e) {
            ScenarioRepository.screenShot();
            log.error("Falha ao clicar no elemento: " + elemento, e);
        }
    }

    protected void clicarElemento(By by){
        this.clicarElemento(
                getDriver().findElements(by).get(0)
        );
    }

    protected void clicarNoElementoPorAtributo(List<WebElement> elements, String attribute, String value) {
        waitProcessPage();
        elements.stream()
                .filter(element -> element.getAttribute(attribute) != null && element.getAttribute(attribute).equals(value))
                .findFirst()
                .ifPresent(this::clicarElemento);
    }

    /**
     * Recupera o texto de um elemento
     *
     * @param elemento elemento que contem um texto
     * @return texto contido no elemento html
     */
    protected String recuperarTexto(WebElement elemento) {
        try {
            waitProcessPage();
            return getDriverWait()
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOf(elemento))
                    .getText();
        } catch (WebDriverException e) {
            return (String) ((JavascriptExecutor) getDriver()).executeScript("return arguments[0].textContent;", elemento);
        } catch (Exception e) {
            ScenarioRepository.screenShot();
            log.error("Falha ao recuperar o texto do elemento: " + elemento, e);
            return null;
        }
    }

    /**
     * Metodo para clicar em um elemento problematico
     *
     * @param elemento elemento mapeado nas pages
     */
    protected void tentarClicarBotaoLoop(WebElement elemento) {
        int tentativas = 0;
        do {
            try {
                waitProcessPage();
                esperarElementoFicarClicavel(elemento);
                clicarElemento(elemento);
                return;
            } catch (Exception ignored) {
            }
            tentativas++;
        } while (tentativas < 10);
    }

    /**
     * Metodo para selecionar uma opção de um Selec List
     *
     * @param elemento selectList mapeado nas pages
     * @param texto    texto da opção desejada
     */
    protected void selecionarItemLista(WebElement elemento, String texto) {
        Select lista = new Select(elemento);
        lista.selectByVisibleText(texto);
    }

    /**
     * Metodo para selecionar uma opção de um Selec List
     *
     * @param elemento selectList mapeado nas pages
     * @param index    posição do elemento
     */
    protected void selecionarItemLista(WebElement elemento, int index) {
        Select lista = new Select(elemento);
        lista.selectByIndex(index);
    }

    /**
     * Validar mensagem pop up
     *
     * @param texto texto a ser verificado
     * @return valor booleano de acordo com a existencia do texto no popup
     */
    public boolean validarMensagemPopUp(String texto) {
        return getDriver().switchTo().alert().getText().contains(texto);
    }

    /**
     * Percorre lista de elementos e clica no que possui o texto desejado
     *
     * @param listaDeElementos lista de Elementos
     * @param texto            texto a ser clicado entre os elementos da lista
     */
    protected void clicarElementoPorTexto(List<WebElement> listaDeElementos, String texto) {
        Optional<WebElement> elemento = listaDeElementos.stream()
                .filter(element -> element.getText().contains(texto))
                .findFirst();

        elemento.ifPresent(this::clicarElemento);
    }

    /**
     * Valida a existencia do elemento na tela
     *
     * @param elemento elemento mapeado
     * @return valor booleano recorrente da existencia do elemento
     */
    protected boolean verificaElementoPresenteTela(WebElement elemento) {
        boolean valor = false;
        try {
            waitProcessPage();
            esperarElementoFicarClicavel(elemento);
            valor = elemento.isDisplayed();
        } catch (Exception e) {
            log.error("Falha ao tentar encontrar o elemento: " + elemento);
        }
        return valor;
    }

    /**
     * Valida a existencia do elemento na tela por texto
     *
     * @param texto texto dentro do elemento
     * @return valor booleano recorrente da existencia do elemento
     */
    public boolean verificaElementoPresenteTela(String texto) {
        boolean valor = false;
        try {
            waitProcessPage();
            esperarElementoFicarClicavel(getDriver().findElement(By.xpath("//*[.='" + texto + "']")));
            valor = getDriver().findElement(By.xpath("//*[.='" + texto + "']")).isDisplayed();
        } catch (Exception e) {
            ScenarioRepository.screenShot();
            log.error("Falha ao tentar encontrar o elemento: " + getDriver().findElement(By.xpath("//*[.='" + texto + "']")));
        }
        return valor;
    }

    /**
     * Realizar scroll ate o elemento
     *
     * @param elemento elemento a ser focalizado
     */
    public void scrollAteOElemento(WebElement elemento) {
        try {
            esperarElementoFicarVisivel(elemento);
            waitProcessPage();
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].scrollIntoView();", elemento);
        } catch (Exception e) {
            ScenarioRepository.screenShot();
            log.error("Falha ao tentar encontrar o elemento: " + elemento);
        }

    }

    /**
     * Realizar scroll ate o fim da pagina
     */
    public void scrollAteOFimDaPagina() {
        waitProcessPage();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Realizar scroll ate o topo da pagina
     */
    public void scrollAteOTopoDaPagina() {
        waitProcessPage();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollTop)");
    }


    protected void uploadArquivo(WebElement elemento, String absolutPath) {
        clicarElemento(elemento);

        Robot robot;
        try {
            robot = new Robot();
            // Coloque o caminho do arquivo na área de transferência
            StringSelection filePath = new StringSelection(absolutPath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePath, null);

            // Use o Robot para colar o caminho do arquivo no diálogo de upload de arquivo e pressionar Enter
            robot.delay(1000);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(1000);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    protected void recarregarTela() {
        waitProcessPage();
        getDriver().get(getDriver().getCurrentUrl());
        waitProcessPage();

        new BasePage();
    }

    /**
     * Tirar um print da pagina para adicionar ao relatório
     */
    public void tirarPrint() {
        waitProcessPage();
        ScenarioRepository.screenShot();
    }

    /**
     * Tirar um print da pagina para adicionar ao relatório
     *
     * @param elementos Elemento(s) para realizar um highlight no print
     */
    public void tirarPrint(WebElement... elementos) {
        waitProcessPage();
        esperarElementoFicarVisivel(elementos);
        ScenarioRepository.screenShot(elementos);
    }

    protected void log(String log) {
        waitProcessPage();
        System.out.println(log);
        ScenarioRepository.addText(log);
    }

    /**
     * Wait com execucao de codigo JS para aguargar o carregamento dos elementos
     */
    private void waitProcessPage() {
        getDriverWait().until(waitProcess());
    }

    /**
     * Condicao personalizada para carrregar os elementos
     */
    private ExpectedCondition<Boolean> waitProcess() {
        return driver -> {
            String js = "var reqAjax = typeof window.Ajax !== 'undefined' ? window.Ajax.activeRequestCount : 0;\n"
                    + "var reqAngular = typeof angular !== 'undefined' ? angular.by(document.body).injector().get('$http').pendingRequests.length : 0;\n"
                    + "var reqJquery = typeof jQuery !== 'undefined' ? jQuery.active : 0;\n"
                    + "var reqDom = document.readyState;\n" + "\n"
                    + "return (reqAjax + reqAngular + reqJquery === 0 && reqDom === 'complete');";

            return ((JavascriptExecutor) getDriver()).executeScript(js).equals(true);
        };
    }

    /**
     * Esperar elemento ficar clicavel
     */
    private void esperarElementoFicarClicavel(WebElement webElement) {
        getDriverWait().until(ExpectedConditions.elementToBeClickable(webElement));
    }

    /**
     * Esperar elemento ficar visivel
     */
    private void esperarElementoFicarVisivel(WebElement... webElement) {
        getDriverWait().until(ExpectedConditions.visibilityOfAllElements(webElement));
    }
}
