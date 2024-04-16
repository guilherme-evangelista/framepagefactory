package br.com.guilhermeevangelista.selenium.test.pages;

import br.com.guilhermeevangelista.selenium.core.driver.BasePage;
import br.com.guilhermeevangelista.selenium.core.utils.PropertiesManager;
import br.com.guilhermeevangelista.selenium.core.utils.variaveis.VariaveisEstaticas;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginSFPage extends BasePage {

    private final PropertiesManager propertiesManagerUsers = new PropertiesManager("user");

    @FindBy (xpath = "//input[@id='username']")
    private WebElement cmpEmail;

    @FindBy (xpath = "//input[@id='password']")
    private WebElement cmpSenha;

    @FindBy (xpath = "//input[@id='Login']")
    private WebElement btnEntrar;

    public void preencherEmail(String user){
        super.digitarTexto(cmpEmail, propertiesManagerUsers.getProp(user.toUpperCase()+"_"+ VariaveisEstaticas.ambiente +"_USER"));
    }

    public void preencherSenha(String user){
        super.digitarTexto(cmpSenha, propertiesManagerUsers.getProp(user.toUpperCase()+"_"+ VariaveisEstaticas.ambiente +"_PASS"));
    }

    public void clicarBotaoEntrar(){
        super.tirarPrint(cmpEmail, cmpSenha, btnEntrar);
        super.clicarElemento(btnEntrar);
    }

    public boolean validarTelaLogin(){
        super.tirarPrint();
        return super.verificaElementoPresenteTela(btnEntrar);
    }

    public boolean aguardarPaginaInicialCarregar() {
        super.tirarPrint(cmpEmail, cmpSenha, btnEntrar);
        return super.verificaElementoPresenteTela(cmpEmail);
    }
}
