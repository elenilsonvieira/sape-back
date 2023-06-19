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
		
		
		driver = new EdgeDriver();
		jse = (JavascriptExecutor)driver;
		login();
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
		Thread.sleep(1000);
		
		driver.get("http://localhost:3000/viewUser");
		Thread.sleep(1000);
		String errorMessage = null;
		Thread.sleep(1000);
		//botão atualizar do perfil
		WebElement updateButton = getElementByXPath("//*[@id=\"root\"]/div[2]/header/fieldset/div/button[2]");
		clickElement(updateButton);
		Thread.sleep(1000);
		
		Thread.sleep(1000);
		//botão atualizar da página de atualização de perfil
		WebElement updateButton2 = getElementByXPath("//*[@id=\"btn-upd\"]");
		Thread.sleep(1000);
		
		
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
		default:
			errorMessage="";
		}
		
		clickElement(updateButton2);
		
		Thread.sleep(1000);
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		Thread.sleep(1000);
		
		assertEquals("Erro", title);
		assertEquals(errorMessage, message);
	}	
	
	@Test
	@Order(2)
	public void UpdatingProfileValid() throws InterruptedException{
		Thread.sleep(2000);
		driver.get("http://localhost:3000/viewUser");
		Thread.sleep(2000);
		
		//botão atualizar do perfil
		WebElement updateButton = getElementByXPath("//*[@id=\"root\"]/div[2]/header/fieldset/div/button[2]");
		clickElement(updateButton);
		Thread.sleep(2000);
		
		Thread.sleep(2000);
		//botão atualizar da página de atualização de perfil
		WebElement updateButton2 = getElementByXPath("//*[@id=\"btn-upd\"]");
		Thread.sleep(2000);
		
		writeFields("ytallopereiralves@gmail.com");
		clickElement(updateButton2);
		Thread.sleep(2000);
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		assertEquals("Sucesso", title);
		assertEquals("Usuario atualizado com sucesso!", message);
		
	}
	
	@Test
	@DisplayName("Desfavoritando esporte")
	@Order(3)
	public void unFavouriteSport() throws InterruptedException{
		Thread.sleep(1000);
		driver.get("http://localhost:3000/viewUser");
		Thread.sleep(2000);
		WebElement favouriteButton = getElementByXPath("//*[@id=\"root\"]/div[2]/header/fieldset/div/button[3]");
		clickElement(favouriteButton);
		Thread.sleep(2000);
		
		WebElement unFavouriteButton = getElementByXPath("//*[@id=\"root\"]/div[2]/header/div/table/tbody/tr/td[2]/td/button");
		clickElement(unFavouriteButton);
		Thread.sleep(2000);
		
		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();
		
		assertEquals("Sucesso", title);
		assertEquals("Esporte Removido dos favoritos", message);
		
	}
	
	@Test
	@DisplayName("Redicionando para criação de agendamento")
	@Order(4)
	public void redirectingToCreateScheduling() throws InterruptedException{
		Thread.sleep(1000);
		driver.get("http://localhost:3000/viewUser");
		Thread.sleep(2000);
		
		WebElement createScheduling = getElementByXPath("//*[@id=\"root\"]/div[2]/header/fieldset/div/button[1]");
		clickElement(createScheduling);
		Thread.sleep(2000);
		
		String currentURL = driver.getCurrentUrl().toString();
		
		assertEquals("http://localhost:3000/createScheduling",currentURL);
	
	}
	
	private void writeFields(String email) throws InterruptedException {
		
		WebElement element;
		if( email != null) {
			element = getElementByXPath("//*[@id=\"email\"]");
			element.clear();
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
	
	private static WebElement getElementById(String id) {
		return driver.findElement(By.id(id));
	}

	private WebElement getElementByClass(String className) {
		return driver.findElement(By.className(className));
	}

	private static WebElement getElementByXPath(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}
	
	private WebElement getElementByTagName(String tag) {
		return driver.findElement(By.tagName(tag));
	}

	private static void login() {
		//abrir página de login
		driver.get("http://localhost:3000/login");
		//prencher campos
		writeLoginFields("201715020017","qwe1238246GILZA");
		//botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		buttonLogin.click();
	}
	
	private static void writeLoginFields(String registration,String password) {
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

}