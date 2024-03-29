package br.edu.ifpb.dac.sape.system;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@TestMethodOrder(OrderAnnotation.class)
public class SchedulingCRUDSystemTest {

	private static WebDriver driver;
	private static JavascriptExecutor jse;
	private static String password = "";

	@BeforeAll
	static void setUp() throws Exception {
		File file = new File("webDriver/chromedriver-win64/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver",
				file.getAbsolutePath());

		driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

	    driver.manage().window().setSize(new Dimension(1552, 840));

		login();
	}

	@AfterEach
	void beforeEach() throws InterruptedException {
		Thread.sleep(2000);
	}

	@AfterAll
	static void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}

	@Test
	@DisplayName("Navegação simples - Agendamento")
	@Order(1)
	public void navigatingScheduling() throws InterruptedException {
		Thread.sleep(1000);

		driver.get("http://localhost:3000");

		Thread.sleep(2000);

		// Navegando para página de listagem de agendamentos
		driver.get("http://localhost:3000/listScheduling");

		assertEquals("http://localhost:3000/listScheduling", driver.getCurrentUrl().toString());

		Thread.sleep(2000);

		// Navegando para página de cadastro de agendamentos por botão
		driver.get("http://localhost:3000/createScheduling");

		assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString());

		Thread.sleep(2000);

		// Cancelando cadastro e voltando para página de listagem

	    driver.findElement(By.cssSelector(".btn-danger")).click();

		assertEquals("http://localhost:3000/listScheduling", driver.getCurrentUrl().toString());

		Thread.sleep(2000);

		// Navegando para página de cadastro de agendamentos pelo navbar
		getElementByXPath("/html/body/div/div[1]/div/div/ul/li[6]/a").click();

		assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString());
	}

	@Test
	@DisplayName("Criando Agendamento - Caso Positivo")
	@Order(2)
	public void createSchedulingOk() throws InterruptedException {
		Thread.sleep(1000);
		driver.get("http://localhost:3000/createScheduling");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedDate = LocalDate.now().plusDays(1).format(dtf);

		writerFields(formattedDate, "15:00", "16:30", "Ginásio", "Futebol");

		WebElement saveButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		saveButton.click();

		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();

		assertEquals("Sucesso", title);
		assertEquals("Prática agendada com sucesso!", message);

		assertEquals("http://localhost:3000/listScheduling", driver.getCurrentUrl().toString());

		// Validações na tela de listagem de agendamento

		WebElement tbodyElement = driver.findElement(
				By.cssSelector("#root > div:nth-child(2) > header > fieldset > fieldset > div > table > tbody"));

		String tableBody = tbodyElement.getText();

		String date = LocalDate.now().plusDays(1).toString();

		Pattern p = Pattern.compile(String.format("\\n?.*%s.*15:00.*16:30.*Ginásio.*", date));
		Matcher m = p.matcher(tableBody);

		final String lineOnTable;
		if (m.find()) {
			lineOnTable = m.group(0);
		} else {
			lineOnTable = "";
		}

		assertAll("Verificando se elemento salvo está na listagem de agendamentos",
				() -> assertTrue(lineOnTable.contains(date)), () -> assertTrue(lineOnTable.contains("15:00")),
				() -> assertTrue(lineOnTable.contains("16:30")), () -> assertTrue(lineOnTable.contains("Ginásio")),
				() -> assertTrue(lineOnTable.contains("Futebol")));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
	@DisplayName("Criando Agendamento - Casos Negativos")
	@Order(3)
	public void createSchedulingFail(int caseFail) throws InterruptedException {
		Thread.sleep(1000);
		driver.get("http://localhost:3000/createScheduling");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String date;

		String errorMessage = null;

		switch (caseFail) {
		// Caso de colisão de agendamentos, passando os mesmos dados do teste
		// CreateSchedulingOk
		case 1:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "15:00", "16:30", "Ginásio", "Futebol");

			errorMessage = "Já existe uma prática agendada para esse horário!";
			break;

		// Caso de horário fora do funcionamento do campus
		case 2:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "06:00", "07:00", "Ginásio", "Futebol");

			errorMessage = "O horário da prática deve ser entre 07:00 e 22:00";
			break;

		// Caso de duração inválida (menor que 0 minutos)
		case 3:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "10:00", "09:00", "Ginásio", "Futebol");

			errorMessage = "A duração da prática não deve ser igual ou menor que 0 minutos!";
			break;

		// Caso de duração inválida (maior que o máximo permitido)
		case 4:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "09:00", "13:00", "Ginásio", "Futebol");

			errorMessage = "A prática agendada deve ter no máximo 180 minutos!";
			break;

		// Caso de prática agendada para o passado
		case 5:
			date = LocalDate.now().minusDays(1).format(dtf);
			writerFields(date, "09:00", "10:00", "Ginásio", "Futebol");

			errorMessage = "A data da prática não pode estar no passado!";
			break;

		// Casos de campos em branco
		case 6:
			writerFields("", "09:00", "10:00", "Ginásio", "Futebol");

			errorMessage = "É obrigatório informar a data em que acontecerá a prática esportiva!";
			break;

		case 7:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "", "10:00", "Ginásio", "Futebol");

			errorMessage = "É obrigatório informar o horário em que a prática esportiva começará!";
			break;

		case 8:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "09:00", "", "Ginásio", "Futebol");

			errorMessage = "É obrigatório informar o horário em que a prática esportiva terminará!";
			break;

		case 9:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "09:00", "10:00", null, "Futebol");

			errorMessage = "É obrigatório selecionar um local!";
			break;

		case 10:
			date = LocalDate.now().plusDays(1).format(dtf);
			writerFields(date, "09:00", "10:00", "Ginásio", null);

			errorMessage = "É obrigatório selecionar um esporte!";
			break;
		}

		Thread.sleep(1000);

		WebElement saveButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		saveButton.click();

		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();

		Thread.sleep(1000);

		assertEquals("Erro", title);
		assertEquals(errorMessage, message);

		assertEquals("http://localhost:3000/createScheduling", driver.getCurrentUrl().toString());
	}

	@Test
	@DisplayName("Excluindo Agendamento")
	@Order(4)
	public void deleteScheduling() throws InterruptedException {
		Thread.sleep(1000);
		driver.get("http://localhost:3000/listScheduling");

		driver.manage().window().setSize(new Dimension(1552, 840));
	    driver.findElement(By.cssSelector(".textEvent")).click();
	    driver.findElement(By.cssSelector(".btn:nth-child(5)")).click();

		
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	@DisplayName("Criando Agendamentos e verificando se estão na tabela")
	@Order(5)
	public void createSchedulings(int cases) throws InterruptedException {
		Thread.sleep(300);
		driver.get("http://localhost:3000/createScheduling");

		String date;

		switch (cases) {
		case 1:
			date = "20/06/2023";
			writerFields(date, "15:00", "17:30", "Feitosão", "Futebol");

			break;

		case 2:
			date = "21/06/2023";
			writerFields(date, "11:00", "13:00", "Quadra", "Futsal");

			break;

		case 3:
			date = "22/06/2023";
			writerFields(date, "08:00", "10:00", "Ginásio", "Volei");

			break;

		case 4:
			date = "23/06/2023";
			writerFields(date, "14:00", "16:00", "AABB", "Natação");

			break;

		}

		WebElement saveButton = getElementByXPath("/html/body/div/div[2]/header/fieldset/button[1]");
		saveButton.click();

		String title = getElementByClass("toast-title").getText();
		String message = getElementByClass("toast-message").getText();

//		assertEquals("Sucesso", title);
//		assertEquals("Prática agendada com sucesso!", message);
//
//		assertEquals("http://localhost:3000/listScheduling", driver.getCurrentUrl().toString());
//
//		// Validações na tela de listagem de agendamento
//
//		WebElement tbodyElement = driver.findElement(
//				By.cssSelector("#root > div:nth-child(2) > header > fieldset > fieldset > div > table > tbody"));
//
//		String tableBody = tbodyElement.getText();
//
//		WebElement dataElement = getElementByXPath(
//				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[1]");
//		String date1 = dataElement.getText();
//		WebElement startElement = getElementByXPath(
//				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[2]");
//		String start = startElement.getText();
//		WebElement finishElement = getElementByXPath(
//				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[3]");
//		String finish = finishElement.getText();
//		WebElement placeElement = getElementByXPath(
//				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[4]");
//		String place = placeElement.getText();
//		WebElement sportElement = getElementByXPath(
//				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[5]");
//		String sport = sportElement.getText();
//
//		Pattern p = Pattern.compile(String.format("\n?.*%s.*%s.*%s.*%s.*%s.*", date1, start, finish, place, sport));
//		Matcher m = p.matcher(tableBody);
//
//		final String lineOnTable;
//		if (m.find()) {
//			lineOnTable = m.group(0);
//		} else {
//			lineOnTable = "";
//		}
//		assertAll("Verificando se elemento salvo está na listagem de agendamentos",
//				() -> assertTrue(lineOnTable.contains(date1)), () -> assertTrue(lineOnTable.contains(start)),
//				() -> assertTrue(lineOnTable.contains(finish)), () -> assertTrue(lineOnTable.contains(place)),
//				() -> assertTrue(lineOnTable.contains(sport)));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	@DisplayName("Testando os filtros de Agenamentos")
	@Order(6)
	public void filterScheduling(int cases) throws InterruptedException {
		Thread.sleep(1000);

		driver.get("http://localhost:3000/listScheduling");

		Thread.sleep(2000);

		String place;
		String sport;
		String date;

		switch (cases) {
		case 1:
			place = "Quadra";
			writeFilters(null, place, null);
			break;

		case 2:
			sport = "Futebol";
			writeFilters(null, null, sport);

			break;

		case 3:
			date = "22/06/2023";
			writeFilters(date, null, null);

			break;

		case 4:
			getElementByXPath("/html/body/div/div[2]/header/fieldset/div/h3/div/button[2]").click();

		}

		WebElement buttonApplyFilter = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/h3/div/button[1]");
		buttonApplyFilter.click();

		Thread.sleep(1000);
		// Navegando para página de listagem de agendamentos
		WebElement tbodyElement = driver.findElement(
				By.cssSelector("#root > div:nth-child(2) > header > fieldset > fieldset > div > table > tbody"));

		String tableBody = tbodyElement.getText();

		WebElement dataElement = getElementByXPath(
				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[1]");
		String dateRes = dataElement.getText();
		WebElement placeElement = getElementByXPath(
				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[4]");
		String placeRes = placeElement.getText();
		WebElement sportElement = getElementByXPath(
				"/html/body/div/div[2]/header/fieldset/fieldset/div/table/tbody/tr/td[5]");
		String sportRes = sportElement.getText();

		Thread.sleep(1500);
		Pattern p = Pattern.compile(String.format("\n?.*%s.*%s.*%s.*", dateRes, placeRes, sportRes));
		Matcher m = p.matcher(tableBody);

		final String lineOnTable;
		if (m.find()) {
			lineOnTable = m.group(0);
		} else {
			lineOnTable = "";
		}
		System.out.println("line+" + lineOnTable);
		assertAll("Verificando se os filtros de agendamentos retornam o agendamento certo",
				() -> assertTrue(lineOnTable.contains(dateRes)), () -> assertTrue(lineOnTable.contains(placeRes)),
				() -> assertTrue(lineOnTable.contains(sportRes)));
	}

	private void writerFields(String date, String startTime, String finishTime, String placeName, String sportName)
			throws InterruptedException {
		WebElement element;

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[1]/input");
		element.sendKeys(date);

		Thread.sleep(800);

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[2]/input");
		element.sendKeys(startTime);

		Thread.sleep(800);

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[3]/input");
		element.sendKeys(finishTime);

		Thread.sleep(800);

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[4]/div/div/div/input");
		if (placeName != null) {
			element.sendKeys(placeName);
			WebElement place = driver.findElement(By.xpath("//ul/li[contains(text(), '" + placeName + "')]"));
			place.click();
		}

		Thread.sleep(800);

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div[5]/div/div/div/input");
		if (sportName != null) {
			element.sendKeys(sportName);
			WebElement sport = driver.findElement(By.xpath("//ul/li[contains(text(), '" + sportName + "')]"));
			sport.click();
		}

		Thread.sleep(1000);
	}

	private static void clickElement(WebElement we) {
		try {
			we.click();
		} catch (Exception e) {
			jse.executeScript("arguments[0].click()", we);
		}
	}

	private static void login() throws InterruptedException {
		// abrir página de login
		driver.get("http://localhost:3000/login");
		// prencher campos
		writeLoginFields("202015020008", password);
		// botão login
		WebElement buttonLogin = getElementByXPath("//button[@class='btn btn-primary']");
		Thread.sleep(500);
		clickElement(buttonLogin);
		Thread.sleep(1500);
	}

	private static void writeFilters(String date, String place, String sport) throws InterruptedException {
		WebElement element;

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/h3/div/div[1]/div/div/div/input");

		if (place != null) {
			element.sendKeys(place);
			WebElement placeOp = driver.findElement(By.xpath("//ul/li[contains(text(), '" + place + "')]"));
			placeOp.click();
		}

		Thread.sleep(800);

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/h3/div/div[2]/div/div/div/input");
		if (sport != null) {

			element.sendKeys(sport);
			WebElement sportOp = driver.findElement(By.xpath("//ul/li[contains(text(), '" + sport + "')]"));
			sportOp.click();

		}

		Thread.sleep(800);

		element = getElementByXPath("/html/body/div/div[2]/header/fieldset/div/h3/div/div[3]/input");
		if (date != null) {
			element.sendKeys(date);

		}

		Thread.sleep(1000);
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

	@SuppressWarnings("unused")
	private WebElement getElementById(String id) {
		return driver.findElement(By.id(id));
	}

	private WebElement getElementByClass(String className) {
		return driver.findElement(By.className(className));
	}

	private static WebElement getElementByXPath(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}

	@SuppressWarnings("unused")
	private WebElement getElementByTagName(String tag) {
		return driver.findElement(By.tagName(tag));
	}
}
