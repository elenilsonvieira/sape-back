package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;

@TestMethodOrder(OrderAnnotation.class)
public class LoginSystemTest {

	private static WebDriver driver;
	private static JavascriptExecutor jse;
	private String password = "";

	@BeforeAll
	static void setUp() throws InterruptedException {
		
		
		
		File file = new File("webDriver/chromedriver-win64/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver",
				file.getAbsolutePath());

		driver = new ChromeDriver();
		jse = (JavascriptExecutor) driver;

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(1000); 
	}

	@AfterAll
	static void tearDown() throws InterruptedException {
		Thread.sleep(1000);
		driver.quit();
	}

	@Test
	@DisplayName("Testar login válido")
	@Order(1)
	public void validLoginTest() throws InterruptedException {
		Thread.sleep(1000);
		// abrir página de login
		driver.get("http://localhost:3000/login");
		// prencher campos
		writeFields("202015020008", this.password);
		// botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonLogin);
		Thread.sleep(1500);

		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Teste de login válido",
				/* aviso de sucesso */
				() -> assertEquals("Sucesso", cardTitle), () -> assertEquals("Bem vindo(a)202015020008", cardMsg),
				/* se o redirecionamento foi feito à página informada */
				() -> assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString()));

		
		Thread.sleep(1000);
		driver.findElement(By.linkText("Sair")).click();
		Thread.sleep(500);
	}

	@ParameterizedTest
	@ValueSource(strings = { "1", "2", "3" })
	@DisplayName("Testar login inválido")
	@Order(2)
	public void invalidLoginTest(String input) throws InterruptedException {
		Thread.sleep(100);
		// abrir página de login
		driver.get("http://localhost:3000/login");

		String errorMessage;

		switch (input) {
		case "1":
			// campo matrícula vazio
			writeFields(null, "12103921312");
			errorMessage = "Login Inválido!";
			break;
		case "2":
			// campo senha vazio
			writeFields("203015020008", null);
			errorMessage = "Login Inválido!";
			break;
		case "3":
			// campos preenchidos porém inválidos
			writeFields("203015020008", password);
			errorMessage = "Login Inválido!";
			break;
		default:
			errorMessage = "";
		}

		Thread.sleep(1500);

		// botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonLogin);

		// card de erro
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Teste de login inválido",
				/* aviso de sucesso */
				() -> assertEquals("Erro", cardTitle), () -> assertEquals(errorMessage, cardMsg));

		Thread.sleep(1500);
	}

	private void writeFields(String registration, String password) {
		WebElement element;

		// campo matricula
		if (registration != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[1]/input");
			element.sendKeys(registration);
		}

		// campo senha referência
		if (password != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[2]/input");
			element.sendKeys(password);
		}

	}

	private void clickElement(WebElement we) {
		try {
			we.click();
		} catch (Exception e) {
			jse.executeScript("arguments[0].click()", we);
		}
	}

	private WebElement getElementById(String id) {
		return driver.findElement(By.id(id));
	}

	private WebElement getElementByXPath(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}

	private WebElement getElementByClass(String className) {
		return driver.findElement(By.className(className));
	}
}
