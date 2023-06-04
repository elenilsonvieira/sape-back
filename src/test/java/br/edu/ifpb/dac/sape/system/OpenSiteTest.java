package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;


public class OpenSiteTest {
	
	private static WebDriver driver;
	

	@BeforeAll
	static void setUp() {
		System.setProperty("webdriver.edge.driver", 
				"C:\\Users\\ytall\\Documents\\workspace-spring-tool-suite-4-4.17.2.RELEASE\\msedgedriver.exe");
		driver = new EdgeDriver();
		// caso não encontre um elemento (em uma busca), espera 10s (fazendo novas buscas) antes de lançar erro.
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); 
	}
	
	@AfterAll
	static void tearDown() {
		driver.quit();
	}

	@Test
	@DisplayName("Acesso a homepage")
	void home() throws InterruptedException {
		driver.get("http://localhost:3000");
		Thread.sleep(2000);
	}
	
	@Test
	@DisplayName("Verifica titulo da aba")
	void title() {
		String title = driver.getTitle();
		assertAll("Página errada",
				() -> assertTrue(title.contentEquals("SAPE")),
				() -> assertTrue(title.length() == 4)
		);
	}

	
}
