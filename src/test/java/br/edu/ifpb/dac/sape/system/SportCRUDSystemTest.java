package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@TestMethodOrder(OrderAnnotation.class)
public class SportCRUDSystemTest {
	
	private static WebDriver driver;
	private static JavascriptExecutor jse;

	@BeforeAll
	public static void setUp() throws InterruptedException {
		System.setProperty("webdriver.edge.driver", 
				"C:\\Users\\ytall\\Documents\\workspace-spring-tool-suite-4-4.17.2.RELEASE\\msedgedriver.exe");
		
		jse = (JavascriptExecutor)driver;
		driver = new EdgeDriver();
		
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	@AfterEach
	public void beforeEach() throws InterruptedException {
		Thread.sleep(2000);
	}

	@AfterAll
	public static void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}

	@Test
	@DisplayName("Cadastrando um novo esporte - Caso Válido")
	@Order(1)
	public void creatingValidSport() throws InterruptedException {
		login();
		Thread.sleep(2000);
		
		driver.get("http://localhost:3000/createSport");
		Thread.sleep(1000);
		writeFields("Basquete");
		Thread.sleep(1000);
		WebElement saveButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		Thread.sleep(1000);
		clickElement(saveButton);
		
		Thread.sleep(2000);
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();

		assertEquals("Sucesso", title);
		assertEquals("Esporte criado com sucesso!", message);
		
		assertEquals("http://localhost:3000/listSports", driver.getCurrentUrl().toString());
		
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3","4"})
	@DisplayName("Cadastrando Esporte - Casos Inválidos")
	@Order(2)
	public void createInvalidSport(String inputs) throws InterruptedException {
		driver.get("http://localhost:3000/createSport");
		
		String errorMessage = null;
		
		switch (inputs) {
			// Passando o nome de um esporte que já existe no banco de dados
			case "1":
				writeFields("Basquete");
				
				errorMessage = "Já existe um esporte com nome Basquete";
				break;
				
			// Tentando cadastrar um esporte com o campo nome em branco
			case "2":
				writeFields("");
				
				errorMessage = "É obrigatório informar o nome do esporte!";
				break;
				
				
			// Passando um nome com mais de três caracteres, mas com um caracter especial
			case "3":
				writeFields("@%$#"); 
				errorMessage = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
				break;
				
			case "4":
				writeFields("Ba"); 
				errorMessage = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
				break;
		}
		
		Thread.sleep(2000);
		
		WebElement saveButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		clickElement(saveButton);
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		Thread.sleep(1000);
		
		assertEquals("Erro", title);
		assertEquals(errorMessage, message);
		
		assertEquals("http://localhost:3000/createSport", driver.getCurrentUrl().toString());
	}
	
	@Test
	@DisplayName("Deletando esporte")
	@Order(3)
	void deletingSport() throws InterruptedException {
	
		driver.get("http://localhost:3000/listSports");
		Thread.sleep(2000);
		
		WebElement element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[1]");
		String name = element.getText();
		Thread.sleep(2000);
		
		WebElement buttonDelete = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[3]/td/button[2]");
		clickElement(buttonDelete);
		Thread.sleep(1000);
		
		String tableBody = getElementByTagName("TBODY").getText();
		
		assertFalse(tableBody.contains(name));
	}
	
	@Test
	@DisplayName("Favoritando esporte - Caso Válido")
	@Order(4)
	public void favouriteSportValid() throws InterruptedException {
		
		driver.get("http://localhost:3000/createSport");
		Thread.sleep(1000);
		writeFields("Basquete");
		Thread.sleep(1000);
		WebElement saveButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		Thread.sleep(1000);
		clickElement(saveButton);
		Thread.sleep(1000);
		driver.get("http://localhost:3000/listSports");
		Thread.sleep(1000);
		//botão de favoritar
		WebElement buttonFavourite = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[3]/td/button[1]");
		Thread.sleep(2000);
		clickElement( buttonFavourite);
		
		Thread.sleep(2000);
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		
		assertAll(
				/*aviso de sucesso*/
				() -> assertEquals("Sucesso", title),
				() -> assertEquals("Você demonstrou interesse nesse esporte!", message));
		
	}
	
	
	@Test
	@DisplayName("Favoritando esporte - Caso Inválido")
	@Order(5)
	public void favouriteSportInvalid() throws InterruptedException {
		
		//botao favoritar
		WebElement buttonFavourite = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[3]/td/button[1]");
		Thread.sleep(2000);
		clickElement( buttonFavourite);
		
		Thread.sleep(2000);
		//título do alerta
		String cardTitle = getElementByClass("toast-title").getText();
	
		assertEquals("Erro", cardTitle);
				
	}
	
	
	
	
	private void login() {
		//abrir página de login
		driver.get("http://localhost:3000/login");
		//prencher campos
		writeLoginFields("202315020008","qwe1238246@GILZA");
		//botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		buttonLogin.click();
	}
	
	private void writeLoginFields(String registration,String password) {
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
	
	private void writeFields(String sportName) throws InterruptedException {
		WebElement element;
		if(sportName != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/input");
			element.sendKeys(sportName);
		}
		
		Thread.sleep(1500);
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

	private WebElement getElementByClass(String className) {
		return driver.findElement(By.className(className));
	}

	private WebElement getElementByXPath(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}
	
	private WebElement getElementByTagName(String tag) {
		return driver.findElement(By.tagName(tag));
	}

}

