package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class OpenSiteTest {

	private static WebDriver driver;

	@BeforeAll
	static void setUp() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\igors\\Downloads\\chromedriver_win32/chromedriver.exe");

		driver = new ChromeDriver();
		// caso não encontre um elemento (em uma busca), espera 10s (fazendo novas
		// buscas) antes de lançar erro.
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
	@DisplayName("Verifica URL da aplicação e título da aba")
	void title() {
		String title = driver.getTitle();
		String url = driver.getCurrentUrl().toString();
		assertAll("Página errada", () -> assertTrue(title.contentEquals("SAPE")),
				() -> assertEquals("http://localhost:3000/", url));
	}

}
