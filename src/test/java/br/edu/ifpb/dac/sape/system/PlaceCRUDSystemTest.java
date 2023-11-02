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
import org.junit.jupiter.api.BeforeEach;
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

import br.edu.ifpb.dac.sape.model.entity.Place;

@TestMethodOrder(OrderAnnotation.class)
public class PlaceCRUDSystemTest {

	private static WebDriver driver;
	private static JavascriptExecutor jse;
	private static Place place;

//	@Autowired
//	private static UserService userService;

	@BeforeAll
	static void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\josej\\Downloads\\chromedriver_win32\\chromedriver.exe");

		driver = new ChromeDriver();
		jse = (JavascriptExecutor) driver;


		place = new Place();
		place.setName("Ar Livre");
		place.setReference("Logo na entrada");
		place.setMaximumCapacityParticipants(100);
		place.setPublic(false);


		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

		login();

	}

	@AfterEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(1000);

	}

	@AfterAll
	public static void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}

	@Test
	@DisplayName("criar local no banco - CASO POSITIVO")
	@Order(1)
	public void createPlaceValid() throws InterruptedException {

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
		WebElement tbodyElement = driver
				.findElement(By.cssSelector("#root > div:nth-child(2) > header > fieldset > div > table > tbody"));

		String tBody = tbodyElement.getText();

		// captura a linha específica que representa o objeto criado
		String lineOnTable = getSpecificLine(tBody, placeName);

		assertAll("Testes do front ao criar place",
				/* aviso de sucesso */
				() -> assertEquals("Sucesso", cardTitle), () -> assertEquals("Local criado com Sucesso!", cardMsg),

				/* se o redirecionamento foi feito à página informada */
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl().toString()),

				/* elementos retornados na tabela */
				() -> assertTrue(lineOnTable.contains(placeName)),
				() -> assertTrue(lineOnTable.contains(placeReference)),
				() -> assertTrue(lineOnTable.contains(placeMaxCapacity)),
				() -> assertTrue(lineOnTable.contains((placeIsPublic) ? "Sim" : "Não")));
		Thread.sleep(1000);
	}

	@ParameterizedTest
	@ValueSource(strings = { "1", "2", "3", "4", "5", "6", "7", "8" })
	@DisplayName("criar local no banco - CASO NEGATIVO por campos em branco e regras de negócio")
	@Order(2)
	void createPlaceFail(String s) throws InterruptedException {

		Thread.sleep(1000);
		driver.get("http://localhost:3000/createPlace");

		String placeName = place.getName();
		String placeReference = place.getReference();
		String placeMaxCapacity = String.valueOf(place.getMaximumCapacityParticipants());
		String responsibleName = "Igor Silva Sobral";

		boolean placeIsPublic = place.isPublic();

		final String messageErro;

		switch (s) {
		case "1":
			writeFields(null, placeReference, placeMaxCapacity, placeIsPublic, responsibleName);
			messageErro = "É obrigatório informar o nome do local!";
			break;
		case "2":
			writeFields(placeName, null, placeMaxCapacity, placeIsPublic, responsibleName);
			messageErro = "É obrigatório informar um local de referência!";
			break;
		case "3":
			writeFields(placeName, placeReference, null, placeIsPublic, responsibleName);
			messageErro = "É obrigatório informar a capacidade máxima do local!";
			break;
		case "4":
			// "Blo" - 3 caracteres
			writeFields("abc", placeReference, placeMaxCapacity, placeIsPublic, responsibleName);
			messageErro = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais";
			break;
		case "5":
			// capacidade não positiva
			writeFields(placeName, placeReference, "0", placeIsPublic, responsibleName);
			messageErro = "A capacidade de participantes deve ser um valor positivo!";
			break;
		case "6":
			// 401 excede a capacidade máxima
			writeFields(placeName, placeReference, "401", placeIsPublic, responsibleName);
			messageErro = "O valor máximo para capacidade de participantes é 400!";
			break;
		case "7":
			// local já está cadastrado no bancod de dados
			writeFields(placeName, placeReference, placeMaxCapacity, placeIsPublic, null);
			messageErro = "É obrigatório informar um responsável pelo local!";
			break;

		case "8":
			// local já está cadastrado no bancod de dados
			writeFields(placeName, placeReference, placeMaxCapacity, placeIsPublic, responsibleName);
			messageErro = "Já existe um local com nome " + placeName;
			break;

		default:
			messageErro = "";
		}

		Thread.sleep(1000);

		// botão salvar
		WebElement buttonSave = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		clickElement(buttonSave);

		Thread.sleep(1000);

		// card de sucesso
		String cardTitle = getElementByClass("toast-title").getText();
		String cardMsg = getElementByClass("toast-message").getText();

		assertAll("Testes do front ao criar place - mesangem de campos",
				/* aviso de falha para cada card */
				() -> assertEquals("Erro", cardTitle), () -> assertEquals(messageErro, cardMsg),

				/* a página ainda deve ser a mesma depois do erro */
				() -> assertEquals("http://localhost:3000/createPlace", driver.getCurrentUrl()));
	}

	@Test
	@DisplayName("Atualizar local - CASO POSITIVO - verificando se dados chegaram nos campos corretamente e botão cancelar")
	@Order(3)
	void updatePlaceValid() throws InterruptedException {

		Thread.sleep(1000);
		driver.get("http://localhost:3000/listPlaces");

		// pegando informações da primeira linha da tabela à mostra para usuário
		String placeName = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[1]")
				.getText();
		String placeReference = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[2]")
				.getText();
		String PlaceCapacity = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[4]")
				.getText();
		String placeIsPublicString = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[5]")
				.getText();
		boolean placeIsPublic = placeIsPublicString.equals("Sim");

		// botão atualizar do primeiro elemento
		WebElement updateButton = getElementByXPath(
				"/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[6]/div/button[1]");
		clickElement(updateButton);

		WebElement nameWE = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[1]/input");
		WebElement referenceWE = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[2]/input");
		WebElement capacityWE = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[3]/input");
		WebElement isPublicWE = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[4]/input");

		// campo já possui valor. Pegando valor que não é editável
//		String placeId = getElementById("lab00").getAttribute("value");

		assertAll("Elemento devem estar nos campus da página de edição",
				/* no campo ID deve conter apenas números */

				/*
				 * conferindo campos: nome, referência e capacidade. Por padrão a caixa de
				 * 'isPublic' não fica marcada
				 */
				() -> assertEquals(nameWE.getAttribute("value"), placeName),
				() -> assertEquals(referenceWE.getAttribute("value"), placeReference),
				() -> assertEquals(capacityWE.getAttribute("value"), PlaceCapacity),
				/*
				 * a página deve ser a de edição. A url contém ao final a id do place que está
				 * sendo editado
				 */
				() -> assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updatePlace/")));

		Thread.sleep(1500);

		// novos valores dos atributos. Modificamos tudo mesno o ID, pois é inacessível
		String newName = "REFEITORIO";
		String newReference = "Próximo ao refeitório";
		String newCapacity = "30";
		boolean newIsPublic = !placeIsPublic;

		nameWE.clear();
		nameWE.sendKeys(newName);
		referenceWE.clear();
		referenceWE.sendKeys(newReference);
		capacityWE.clear();
		capacityWE.sendKeys(newCapacity);
		// inverte o valor bolleano atual de isPublic
		if (newIsPublic) {
			clickElement(isPublicWE);
		}

		Thread.sleep(1500);

		// clicando no botão de Atualizar
		WebElement buttonUpdate = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		Thread.sleep(1000);
		clickElement(buttonUpdate);
		WebElement tbodyElement = driver
				.findElement(By.cssSelector("#root > div:nth-child(2) > header > fieldset > div > table > tbody"));
		Thread.sleep(1000);
		// pegando todas as tuplas da tabela
		String tBody = tbodyElement.getText();

		// pegando linha específica do objeto que foi atualizado
		String lineOnTable = getSpecificLine(tBody, newName);
		System.err.println(lineOnTable);
		assertAll("Verificando valores após edição de todos, assim como verifica card",
				() -> assertEquals("Sucesso", getElementByClass("toast-title").getText()),
				() -> assertEquals("Local atualizado com sucesso!", getElementByClass("toast-message").getText()),
				() -> assertTrue(lineOnTable.contains(newName)), () -> assertTrue(lineOnTable.contains(newReference)),
				() -> assertTrue(lineOnTable.contains(newCapacity)),
				() -> assertTrue(lineOnTable.contains((newIsPublic) ? "Sim" : "Não")),
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl()));

		Thread.sleep(1500);

		/* clicando no botão cancelar ao carregar a página de update */

		// clicando no botão de Atualizar da página de listagem
		WebElement updateButton02 = getElementByXPath(
				"/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[6]/div/button[1]");
		clickElement(updateButton02);

		Thread.sleep(500);

		// a página atual deve ser a de edição. No final da Url há o Id do place.
		assertTrue(driver.getCurrentUrl().contains("http://localhost:3000/updatePlace/"));

		WebElement buttonCancel = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[2]");
		clickElement(buttonCancel);

		Thread.sleep(500);

		// a página deve ser a de listagem dos locais
		assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl());
	}

	@Test
	@DisplayName("Deletando um local iniciando da página de listagem")
	@Order(4)
	void deletePlace() throws InterruptedException {
		Thread.sleep(2000);
		driver.get("http://localhost:3000/listPlaces");

		// nome do primeiro local da lista
		String name = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[1]").getText();

		Thread.sleep(1000);
		// precionar o primeiro botão "excluir" da tabela
		WebElement buttonExclude = getElementByXPath(
				"/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[6]/div/button[2]");
		clickElement(buttonExclude);

		// necessário para que a tabela tenha tempo de ser atualizada
		Thread.sleep(1000);

		WebElement tbodyElement = driver
				.findElement(By.cssSelector("#root > div:nth-child(2) > header > fieldset > div > table > tbody"));

		String tBody = tbodyElement.getText();

		String lineWithId = getSpecificLine(tBody, name);

		assertAll("Exclusão de local", () -> assertTrue(lineWithId.isEmpty()),
				() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl()));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	@DisplayName("Criando lOCAIS e verificando se estão na tabela")
	@Order(5)
	public void createPlaces(int cases) throws InterruptedException {
		Thread.sleep(1000);
		driver.get("http://localhost:3000/createPlace");

		String name;
		String reference;
		String capacity;
		boolean isPublic = true;
		String responsible = "Igor Silva Sobral";

		switch (cases) {
		case 1:
			name = "AABB";
			reference = "Perto da BR";
			capacity = "25";
			writeFields(name, reference, capacity, isPublic, responsible);

			break;

		case 2:
			name = "Ginásio";
			reference = "Do Lado do Estacionamento";
			capacity = "25";
			writeFields(name, reference, capacity, isPublic, responsible);
			break;

		case 3:
			name = "Quadra";
			reference = "Escola Estadual";
			capacity = "25";
			writeFields(name, reference, capacity, isPublic, responsible);

			break;

		case 4:
			name = "Feitosão";
			reference = "Atrás do Hospital";
			capacity = "25";
			writeFields(name, reference, capacity, isPublic, responsible);

			break;

		}

		Thread.sleep(500);
		WebElement saveButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		saveButton.click();

		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();

		assertAll(() -> assertEquals("http://localhost:3000/listPlaces", driver.getCurrentUrl().toString()),
				() -> assertEquals("Sucesso", title), () -> assertEquals("Local criado com Sucesso!", message)

		);

		// Validações na tela de listagem de agendamento

		WebElement tbodyElement = driver
				.findElement(By.cssSelector("#root > div:nth-child(2) > header > fieldset > div > table > tbody"));

		String tableBody = tbodyElement.getText();

		// captura a linha específica que representa o objeto criado

		WebElement nameElement = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[1]");
		String placeName = nameElement.getText();
		WebElement referElement = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[2]");
		String referenceValue = referElement.getText();
		WebElement capacityElement = getElementByXPath(
				"/html/body/div/div[2]/header/fieldset/div/table/tbody/tr/td[3]");
		String capacityValue = capacityElement.getText();

		Pattern p = Pattern.compile(String.format("\n?.*%s.*%s.*%s.*", placeName, referenceValue, capacityValue));
		Matcher m = p.matcher(tableBody);

		final String lineOnTable;
		if (m.find()) {
			lineOnTable = m.group(0);
		} else {
			lineOnTable = "";
		}
		assertAll("Verificando se elemento salvo está na listagem de Locais",
				() -> assertTrue(lineOnTable.contains(placeName)),
				() -> assertTrue(lineOnTable.contains(referenceValue)),
				() -> assertTrue(lineOnTable.contains(capacityValue))

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

	private void writeFields(String placeName, String reference, String capacityM, boolean isPublic,
			String responsibleName) throws InterruptedException {
		WebElement element;

		// caixa nome
		if (placeName != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[1]/input");
			element.sendKeys(placeName);
		}

		// caixa referência
		if (reference != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[2]/input");
			element.sendKeys(reference);
		}

		// caixa capacidade total
		if (capacityM != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[3]/input");
			element.sendKeys(capacityM);
		}

		// caixa "é público?"
		if (isPublic) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[4]/input");
			clickElement(element);
			Thread.sleep(3000);
		}
		// nome do responsável
		if (responsibleName != null) {
			element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[5]/div/div/div/input");
			element.sendKeys(responsibleName);
			WebElement option = driver.findElement(By.xpath("//ul/li[contains(text(), '" + responsibleName + "')]"));
			option.click();

		}
	}

	private void writeFieldsUpdate(String placeName, String reference, String capacityM, boolean isPublic) {
		WebElement element;

		element = getElementById("lab01");
		clearField(element);
		if (placeName != null) {
			element.sendKeys(placeName);
		}

		element = getElementById("lab02");
		clearField(element);
		if (reference != null) {
			element.sendKeys(reference);
		}

		element = getElementById("lab03");
		clearField(element);
		if (capacityM != null) {
			element.sendKeys(capacityM);
		}

		if (isPublic) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			WebElement we = getElementByClass("form-check-input");
			try {
				we.click();
			} catch (Exception e) {
				jse.executeScript("arguments[0].click()", we);
			}
		}
	}

	private static void clickElement(WebElement we) {
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

	private static void login() throws InterruptedException {
		// abrir página de login
		driver.get("http://localhost:3000/login");
		// prencher campos
		writeLoginFields("201915020021", "");
		// botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		clickElement(buttonLogin);
		Thread.sleep(1500);
	}

	private static void writeLoginFields(String registration, String password) {
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

	private WebElement getElementById(String id) {
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
}