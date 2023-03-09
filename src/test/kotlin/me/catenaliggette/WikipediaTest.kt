package me.catenaliggette

import io.github.bonigarcia.wdm.WebDriverManager
import io.qameta.allure.Step
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation::class)
class WikipediaTest {
    private lateinit var driver: WebDriver

    @BeforeAll
    fun before() {
        WebDriverManager.firefoxdriver().setup()
        driver = FirefoxDriver()
        driver.manage().window().maximize()
    }

    @AfterAll
    fun after() {
        driver.quit()
    }

    @Test
    @Order(2)
    fun `test search on wikipedia leads to page`() {
        sendKeys("Psychrolutes marcidus", "searchInput")
        Thread.sleep(2000)
        click("cdx-menu-item-2")
        Thread.sleep(4000)
        checkURL("https://en.wikipedia.org/wiki/Psychrolutes_marcidus")
    }


    @Test
    @Order(1)
    fun `test wikipedia log in`() {
        val pageUrl = "https://en.wikipedia.org/wiki/Main_Page"
        openPage(pageUrl)
        click("pt-login-2")
        Thread.sleep(1000)
        sendKeys("Eythg45", "wpName1")
        sendKeys("eh8FaaXV9Yv9gy7", "wpPassword1")
        Thread.sleep(2000)
        click("wpLoginAttempt")
        Thread.sleep(2000)
        checkText("Eythg45", "pt-userpage-2")
        checkURL(pageUrl)
    }

    @Test
    @Order(3)
    fun `test wikipedia log out`() {
        click("vector-user-links-dropdown-checkbox")
        Thread.sleep(500)
        click("pt-logout")
        Thread.sleep(2000)
        checkLoggedOutUrl("https://en.wikipedia.org/w/index.php?title=Special:UserLogout")
        checkText("Log in", "pt-login-2")
    }


    @Step("open {url}")
    fun openPage(url: String) {
        driver.get(url)
    }


    @Step("insert text {text} into field {elementID}")
    fun sendKeys(text: String, elementID: String) {
        val element = driver.findElement(By.id(elementID))
        element.sendKeys(text)
    }

    @Step("Click element")
    fun click(elementID: String) {
        val element = driver.findElement(By.id(elementID))
        element.click()
    }

    @Step("Check url {url}")
    fun checkURL(url: String) {
        assertEquals(url, driver.currentUrl)
    }

    @Step("Check url {url} after log out")
    fun checkLoggedOutUrl(url: String) {
        println(driver.currentUrl.substringBefore("&"))
        assertEquals(url, driver.currentUrl.substringBefore("&"))
    }


    @Step("Check text in username/ log in field")
    fun checkText(username: String, elementID: String) {
        val actualUser = driver.findElement(By.id(elementID)).text
        println(actualUser)
        assertEquals(username, actualUser)
    }
}