package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.User;



@TestMethodOrder(OrderAnnotation.class)
public class PlaceCRUDSystemTest{

	private static WebDriver driver;
	private static JavascriptExecutor jse;
	private static Place place;
	
//	@Autowired
//	private static UserService userService;

	@BeforeAll
<<<<<<< HEAD
	static void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", 
				"C:\\Users\\igors\\Downloads\\chromedriver_win32/chromedriver.exe");
=======
	public static void setUp() throws Exception {
		System.setProperty("webdriver.edge.driver", 
				"C:\\Users\\ytall\\Documents\\workspace-spring-tool-suite-4-4.17.2.RELEASE\\msedgedriver.exe");
>>>>>>> 7638e937dde1a654b10280120437681bcdbbcdcb
		
		driver = new ChromeDriver();
		jse = (JavascriptExecutor)driver;
	
//		responsible = new User();
//		responsible.setName("Ytallo Pereira Alves");
//		responsible.setRegistration(202115020009L);
//		userService.save(responsible);
		
		place = new Place();
		place.setName("Ar Livre");
		place.setReference("Logo na entrada");
		place.setMaximumCapacityParticipants(100);
		place.setPublic(false);
//		Set<User>responsibles = place.getResponsibles();
//		responsibles.add(responsible);
//		place.setResponsibles(responsibles);
		
		// caso não encontre um elemento (em uma busca), espera n segundos (fazendo
		// novas buscas) antes de lançar erro. OBS: o getCurrentURL não se enquadra.
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
	}

	@AfterEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(1000);
		login();
		
	}

	@AfterAll
	public static void tearDown() throws InterruptedException {
		Thread.sleep(1000);
		driver.quit();
	}

	@Test
	@DisplayName("criar local no banco - CASO POSITIVO")
	@Order(1)
	public void createPlaceValid() throws InterruptedException {
		login();
		Thread.sleep(2000);
		driver.get("http://localhost:3000/createPlace");
		
		String placeName = place.getName();
		String placeReference = place.getReference();
		String placeMaxCapacity = String.valueOf(place.getMaximumCapacityParticipants());
		String responsibleName = "Igor Silva Sobral";
		
		boolean placeIsPublic = place.isPublic();
		
		Thread.sleep(2000);
		// Campos de preenchiemtno sendo setados com valores respectivos de:
		writeFields(placeName, placeReference, placeMaxCapacity, placeIsPublic, responsibleName);
		Thread.sleep(2000);
		// botão salvar
		WebElement buttonSave = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		clickElement(buttonSave);
		
		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();
	
		// pega todos os elementos da tabela
		WebElement tbodyElement = driver.findElement(By.cssSelector("#root > div:nth-child(2) > header > fieldset > div > table > tbody"));
		
		String tBody = tbodyElement.getText();
		
		// captura a linha específica que representa o objeto criado
		String lineOnTable = getSpecificLine(tBody, placeName);
		
		
		assertAll("Testes do front ao criar place",
				/*aviso de sucesso*/
				() -> assertEquals("Sucesso", cardTitle),
				() -> assertEquals("Local criado com Sucesso!", cardMsg),
				
				/*se o redirecionamento foi feito à página informada*/
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl().toString()),
				
				/*elementos retornados na tabela*/
				() -> assertTrue(lineOnTable.contains(placeName)),
				() -> assertTrue(lineOnTable.contains(placeReference)),
				() -> assertTrue(lineOnTable.contains(placeMaxCapacity)),
				() -> assertTrue(lineOnTable.contains((placeIsPublic) ? "Sim" : "Não"))
		);
	}

	
	@ParameterizedTest
	@ValueSource(strings = {"1", "2", "3", "4", "5", "6", "7"})
	@DisplayName("criar local no banco - CASO NEGATIVO por campos em branco e regras de negócio")
	@Order(2)
	void createPlaceFail(String s) throws InterruptedException {
		
		Thread.sleep(2000);
		driver.get("http://localhost:3000/createPlace");
		
		String placeName = place.getName();
		String placeReference = place.getReference();
		String placeMaxCapacity = String.valueOf(place.getMaximumCapacityParticipants());
		String responsibleName = "Igor Silva Sobral";
		
		boolean placeIsPublic = place.isPublic();
		
		final String messageErro;
		
		switch (s) {
		case "1":
			writeFields(null, placeReference, placeMaxCapacity, placeIsPublic,responsibleName);
			messageErro = "É obrigatório informar o nome do local!";
			break;
		case "2":
			writeFields(placeName, null, placeMaxCapacity, placeIsPublic,responsibleName);
			messageErro = "É obrigatório informar um local de referência!";
			break;
		case "3":
			writeFields(placeName, placeReference, null, placeIsPublic,responsibleName);
			messageErro = "É obrigatório informar a capacidade máxima do local!";
			break;
		case "4":
			// "Blo" - 3 caracteres
			writeFields("abc", placeReference, placeMaxCapacity, placeIsPublic,responsibleName);
			messageErro = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
			break;
		case "5":
			// capacidade não positiva
			writeFields(placeName, placeReference, "0", placeIsPublic,responsibleName);
			messageErro = "A capacidade de participantes deve ser um valor positivo!";
			break;
		case "6":
			// 401 excede a capacidade máxima
			writeFields(placeName, placeReference, "401", placeIsPublic,responsibleName); 
			messageErro = "O valor máximo para capacidade de participantes é 400!";
			break;
		case "7":
			// local já está cadastrado no bancod de dados
			writeFields(placeName, placeReference, placeMaxCapacity, placeIsPublic,responsibleName);
			messageErro = "Já existe um local com nome " + placeName ;
			break;
//		case "8":
//			// local já está cadastrado no bancod de dados
//			writeFields(placeName, placeReference, placeMaxCapacity, placeIsPublic,null);
//			messageErro = "Já existe um local com nome " + placeName ;
//			break;
		default:
			messageErro = "";
		}
		
		Thread.sleep(1500);
		
		// botão salvar
		WebElement buttonSave = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		clickElement(buttonSave);

		Thread.sleep(500);
		
		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Testes do front ao criar place - mesangem de campos",
				/*aviso de falha para cada card*/
				() -> assertEquals("Erro", cardTitle),
				() -> assertEquals(messageErro, cardMsg),
				
				/*a página ainda deve ser a mesma depois do erro*/
				() -> assertEquals("http://localhost:3000/createPlace", driver.getCurrentUrl())
		);
	}
	
	private String getSpecificLine(String tBody, String idOrName) {
	    String[] lines = tBody.split("\\n");
	    
	    for (String line : lines) {
	        if (line.contains(idOrName)) {
	            return line.trim();
	        }
	    }
	    
	    return ""; 
	}

	
	private void writeFields(String placeName, String reference, String capacityM, boolean isPublic, String responsibleName) throws InterruptedException {
		WebElement element;
		
		// caixa nome
		if(placeName != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[1]/input");
			element.sendKeys(placeName);
		}
		
		// caixa referência
		if(reference != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[2]/input");
			element.sendKeys(reference);
		}

		// caixa capacidade total
		if(capacityM != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[3]/input");
			element.sendKeys(capacityM);
		}

		// caixa "é público?"
		if(isPublic) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[4]/input");
			clickElement(element);
			Thread.sleep(3000);
		}
		//nome do responsável
		if(responsibleName != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[5]/div/div/div/input");
<<<<<<< HEAD
			element.sendKeys(responsibleName);
			clickElement(element);
			
=======
			element.click();
			element.sendKeys(responsibleName);
			Select select=new Select(element); 
			select.selectByVisibleText(responsibleName);
		
>>>>>>> 7638e937dde1a654b10280120437681bcdbbcdcb
		}
	}
	
	private void writeFieldsUpdate(String placeName, String reference, String capacityM, boolean isPublic) {
		WebElement element;

		element = getElementById("lab01");
		clearField(element);
		if(placeName != null) {
			element.sendKeys(placeName);
		}
		
		element = getElementById("lab02");
		clearField(element);
		if(reference != null) {
			element.sendKeys(reference);
		}
		
		element = getElementById("lab03");
		clearField(element);
		if(capacityM != null) {
			element.sendKeys(capacityM);
		}
		
		if(isPublic) {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			WebElement we = getElementByClass("form-check-input");
			try {
				we.click();
			} catch (Exception e) {
				jse.executeScript("arguments[0].click()", we);
			}
		}
	}
	
	private void clickElement(WebElement we) {
		try {
			we.click();
		} catch (Exception e) {
			jse.executeScript("arguments[0].click()", we);
		}
	}

	// Método criado devido a um bug encontrado ao usar clear() do WebElement
	private void clearField(WebElement element) {
		String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
		element.sendKeys(del);
	}
	
	private void login() {
		//abrir página de login
		driver.get("http://localhost:3000/login");
		//prencher campos
		writeLoginFields("201915020021","99458444e.");
		//botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonLogin);
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
