package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class ViewUserProfileSystemTest {
	
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
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3","4"})
	@DisplayName("Atualizando perfil - Caso Inválido")
	@Order(1)
	public void UpdatingProfileInvalid(String inputs) throws InterruptedException {
		login();
		Thread.sleep(2000);
		driver.get("http://localhost:3000/viewUser");
		Thread.sleep(2000);
		String errorMessage = null;
		Thread.sleep(2000);
		//botão atualizar do perfil
		WebElement updateButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/div[4]/button[2]");
		clickElement(updateButton);
		Thread.sleep(2000);
		//botão atualizar da página de atualização de perfil
		WebElement updateButton2 = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/button[1]");
		Thread.sleep(2000);
	
		//passando entradas inválidas
		switch (inputs) {
		
		case "1":
			writeFields("ytallopereiralves@");
			
			errorMessage = "Informe um email válido!";
			break;
			
		case "2":
			writeFields("ytallopereiralves@gmail");
			
			errorMessage = "Informe um email válido!";
			break;
		
		case "3":
			writeFields("ytalloalves");
			
			errorMessage = "Informe um email válido!";
			break;
			
		case "4":
			writeFields("@$%&");
			
			errorMessage = "Informe um email válido!";
			break;
		}
	
		clickElement(updateButton2);
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		Thread.sleep(2000);
		assertEquals("Erro", title);
		assertEquals(errorMessage, message);
	}
	
	@Test
	@DisplayName("Desfavoritando esporte")
	@Order(2)
	public void unFavouriteSport() throws InterruptedException{
		
		login();
		Thread.sleep(2000);
		driver.get("http://localhost:3000/viewUser");
		Thread.sleep(2000);
		WebElement unFavouriteButton = getElementByXPath("html/body/div/div[2]/header/fieldset/div/div[5]/table/tbody/tr/td[2]/td/button");
		clickElement(unFavouriteButton);
		Thread.sleep(2000);
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		assertEquals("Sucesso", title);
		assertEquals("Esporte Removido dos favoritos", message);
		
	}
	
	@Test
	@DisplayName("Redicionando para criação de agendamento")
	@Order(3)
	public void redirectingToCreateScheduling() throws InterruptedException{
		
		login();
		Thread.sleep(2000);
		driver.get("http://localhost:3000/viewUser");
		Thread.sleep(2000);
		WebElement createScheduling = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/div[4]/button[1]");
		clickElement(createScheduling);
		Thread.sleep(2000);
		String currentURL = driver.getCurrentUrl().toString();
		
		assertEquals("http://localhost:3000/createScheduling",currentURL);
	
	}
	
	
	
	private void login() {
		//abrir página de login
		driver.get("http://localhost:3000/login");
		//prencher campos
		writeLoginFields("201715020017","qwe1238246GILZA");
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
	
	
	private void writeFields(String email) throws InterruptedException {
		
		WebElement element;
		if( email != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/div[2]/input");
			
			element.sendKeys(email);
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