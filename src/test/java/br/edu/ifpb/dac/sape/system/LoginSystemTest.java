package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.openqa.selenium.edge.EdgeDriver;
@TestMethodOrder(OrderAnnotation.class)
public class LoginSystemTest {
	
	private static WebDriver driver;
	private static JavascriptExecutor jse;
	
	@BeforeAll
	static void setUp() throws InterruptedException {
		System.setProperty("webdriver.edge.driver", 
				"C:\\Users\\ytall\\Documents\\workspace-spring-tool-suite-4-4.17.2.RELEASE\\msedgedriver.exe");
		
		driver = new EdgeDriver();
		jse = (JavascriptExecutor)driver;
		
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
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3"})
	@DisplayName("Testar login inválido")
	@Order(1)
	public void invalidLoginTest(String input) throws InterruptedException{
		
		//abrir página de login
		driver.get("http://localhost:3000/login");
		
		String errorMessage;
		
		switch (input) {
		case "1":
			//campo matrícula vazio
			writeFields(null,"12103921312");
			errorMessage = "Login Inválido!";
			break;
		case "2":
			//campo senha vazio
			writeFields("203015020008",null);
			errorMessage = "Login Inválido!";
			break;
		case "3":
			//campos preenchidos porém inválidos
			writeFields("203015020008","asud29281288");
			errorMessage = "Login Inválido!";
			break;
		default:
			errorMessage = "";
		}
		
		Thread.sleep(1500);
		
		//botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonLogin);
		
		// card de erro
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();
		
		assertAll("Teste de login inválido",
				/*aviso de sucesso*/
				() -> assertEquals("Erro", cardTitle),
				() -> assertEquals(errorMessage, cardMsg));
				
	}
	
	@Test
	@DisplayName("Testar login válido")
	@Order(2)
	public void validLoginTest() {
		//abrir página de login
		driver.get("http://localhost:3000/login");
		//prencher campos
		writeFields("201715020017","qwe1238246GILZA");
		//botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonLogin);
		
		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();
		
		assertAll("Teste de login válido",
				/*aviso de sucesso*/
				() -> assertEquals("Sucesso", cardTitle),
				() -> assertEquals("Bem vindo(a)201715020017", cardMsg),
				/*se o redirecionamento foi feito à página informada*/
				() -> assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString()));
		
	}
	
	
	
	private void writeFields(String registration,String password) {
		WebElement element;
		
		// campo matricula
		if(registration != null) {
			element = getElementById("input1");
			element.sendKeys(registration);
		}
		
		// campo senha referência
		if(password != null) {
			element = getElementById("input2");
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
