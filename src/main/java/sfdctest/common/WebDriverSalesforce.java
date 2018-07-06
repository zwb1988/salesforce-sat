package sfdctest.common;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WebDriverSalesforce {
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String LOGIN = "Login";

    private String webDriverPath;
    private String url;
    private WebDriver webDriver;

    public WebDriverSalesforce(String url) {
        this(Constant.CHROME_WEB_DRIVER_PATH, url);
    }

    public WebDriverSalesforce(String webDriverPath, String url) {
        this.webDriverPath = webDriverPath;
        this.url = url;
        this.initialize();
    }

    private void initialize() {
        String userDir = System.getProperty(Constant.USER_DIR);
        Path chromeDriverPath = Paths.get(userDir, webDriverPath);
        System.setProperty(Constant.CHROME_WEB_DRIVER, chromeDriverPath.toString());
        if (!Files.exists(chromeDriverPath)) {
            this.unpackChromeDriver(chromeDriverPath);
        }
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 1);
        //1-Allow, 2-Block, 0-default
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("useAutomationExtension", false);
        ChromeDriver chromeDriver = new ChromeDriver(options);
        //chromeDriver.get(GlobalConstant.QA_URL);
        chromeDriver.get(url);
        chromeDriver.manage().window().maximize();
        this.webDriver = chromeDriver;
    }

    public void unpackChromeDriver(Path outputPath) {
        OutputStream os = null;
        try {
            InputStream is = getClass().getResource(Constant.CHROME_WEB_DRIVER_PATH).openStream();
            os = new FileOutputStream(outputPath.toString());

            byte[] buffer = new byte[2048];
            int length;

            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String userName, String password) throws InterruptedException {
        Thread.sleep(2000);
        webDriver.findElement(By.id(USER_NAME)).sendKeys(userName);
        webDriver.findElement(By.id(PASSWORD)).sendKeys(password);
        WebElement loginButton = webDriver.findElement(By.id(LOGIN));
        loginButton.click();
    }

    public WebDriver getWebDriver() {
        return this.webDriver;
    }
}

