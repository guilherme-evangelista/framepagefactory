package br.com.guilhermeevangelista.selenium.core.utils.variaveis;

import br.com.guilhermeevangelista.selenium.core.utils.PropertiesManager;

public class VariaveisEstaticas {
    private static final PropertiesManager propertiesManager = new PropertiesManager("config");
    public static String ambiente = propertiesManager.getProp("ambiente");
}
