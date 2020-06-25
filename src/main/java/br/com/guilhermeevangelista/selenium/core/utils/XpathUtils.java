package br.com.guilhermeevangelista.selenium.core.utils;

import br.com.guilhermeevangelista.selenium.core.driver.BasePage;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class XpathUtils extends BasePage{

    public List<String> retornarListDeStringAPartirDeUmaListaDeElementos(WebElement... webElements){
        List<String> retorno = new ArrayList<>();
        for (WebElement webElement : webElements){
            retorno.add(super.recuperarTexto(webElement));
        }
        return retorno;
    }

}
