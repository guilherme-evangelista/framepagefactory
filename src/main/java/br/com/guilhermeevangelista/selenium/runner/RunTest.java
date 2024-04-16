package br.com.guilhermeevangelista.selenium.runner;

import br.com.guilhermeevangelista.selenium.core.utils.report.Report;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

/**
 * Gerar report com o plugin ClueCumber baseado no custom.css acima
 * @author Guilherme-Evangelista
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome = true,
        dryRun = false,
        plugin = {"json:target/cucumber-report/cucumber.json"},
        features = {"src/main/resources/features"},
        glue = {"br/com/guilhermeevangelista/selenium/test/steps", "br/com/guilhermeevangelista/selenium/core/hooks"},

        tags = {"@loginsucesso"}
)
public class RunTest {
}

class RunTestReport{
    @Test
    public void gerarRelatorio() {
        Report.gerarRelarotioClueCumber();
    }
}