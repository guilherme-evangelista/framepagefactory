package br.com.guilhermeevangelista.selenium.test.pages;

import br.com.guilhermeevangelista.selenium.core.driver.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    @FindBy(xpath = "//a[.='Home']")
    private WebElement btnHome;

    @FindBy(xpath = "//input[@ placeholder ='Search Salesforce']")
    private WebElement cmpPesquisar;

    public boolean validarTelaHomeAtiva(){
        super.tirarPrint();
        return super.verificaElementoPresenteTela(cmpPesquisar);
    }
}
