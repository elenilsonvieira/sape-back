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
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.sape.business.service.UserService;
import br.edu.ifpb.dac.sape.model.entity.Place;
import br.edu.ifpb.dac.sape.model.entity.User;



@TestMethodOrder(OrderAnnotation.class)
public class PlaceCRUDSystemTest{

	private static WebDriver driver;
	private static JavascriptExecutor jse;
	private static Place place;
	private static User responsible;
//	@Autowired
//	private static UserService userService;

	@BeforeAll
	static void setUp() throws Exception {
		System.setProperty("webdriver.edge.driver", 
				"C:\\Users\\ytall\\Documents\\workspace-spring-tool-suite-4-4.17.2.RELEASE\\msedgedriver.exe");
		
		driver = new EdgeDriver();
		jse = (JavascriptExecutor)driver;
	
//		responsible = new User();
//		responsible.setName("Ytallo Pereira Alves");
//		responsible.setRegistration(202115020009L);
//		userService.save(responsible);
		
		place = new Place();
		place.setName("Quadra");
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
	}

	@AfterAll
	static void tearDown() throws InterruptedException {
		Thread.sleep(1000);
		driver.quit();
	}

	@Test
	@DisplayName("criar local no banco - CASO POSITIVO")
	@Order(1)
	public void createPlace() throws InterruptedException {
		login();
		Thread.sleep(2000);
		driver.get("http://localhost:3000/createPlace");
		
		String placeName = place.getName();
		String placeReference = place.getReference();
		String placeMaxCapacity = String.valueOf(place.getMaximumCapacityParticipants());
		String responsibleName = "Igor Silva";
		
		boolean placeIsPublic = place.isPublic();
		Thread.sleep(2000);
		// Campos de preenchiemtno sendo setados com valores respectivos de:
		writeFields(placeName, placeReference, placeMaxCapacity, placeIsPublic, responsibleName);
		Thread.sleep(2000);
		// botão salvar
		WebElement buttonSave = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonSave);
		
		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		// pega todos os elementos da tabela
		String tBody = getElementByTagName("TBODY").getText();
		
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
	
	
	
	private String getSpecificLine(String tBody, String idOrName) {
		Pattern pattern;
		Matcher matcher;
		String lineOnTable = "";
		
		// se conter apenas dígitos
		if(idOrName.matches("^\\d+$")) {
			pattern = Pattern.compile(String.format("\\n?%s.*", idOrName));
		} else {
			pattern = Pattern.compile(String.format("\\n?\\d+ %s.*", idOrName));
		}
		
		matcher = pattern.matcher(tBody);

		if(matcher.find()) { // método find é NECESSÁRIO para iniciar a busca da parte especificada pelo regex.
			lineOnTable = matcher.group(0); // captura toda a expressão
		}
		
		return lineOnTable;
	}
	
	private void writeFields(String placeName, String reference, String capacityM, boolean isPublic, String responsibleName) {
		WebElement element;
		
		// caixa nome
		if(placeName != null) {
			element = getElementById("lab");
			element.sendKeys(placeName);
		}
		
		// caixa referência
		if(reference != null) {
			element = getElementByClass("form-control");
			element.sendKeys(reference);
		}

		// caixa capacidade total
		if(capacityM != null) {
			element = getElementByClass("form-control-small");
			element.sendKeys(capacityM);
		}

		// caixa "é público?"
		if(isPublic) {
			element = getElementById("flexCheckDefault");
			clickElement(element);
		}
		//nome do responsável
		if(responsibleName != null) {
			element = getElementByClass("filterUser");
			element.sendKeys(responsibleName);
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
		writeLoginFields("201715020017","qwe1238246GILZA");
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
