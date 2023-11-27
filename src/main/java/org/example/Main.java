package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

class Page{
    public WebDriver driver;
    public Page(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Elish\\Downloads\\chromedriver-win64\\chromedriver.exe");
        System.setProperty("webdriver.chrome.bin", "C:\\Program Files\\Google\\Chrome Beta\\Application\\chrome.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        this.driver = new ChromeDriver(options);
        this.driver.manage().window().maximize();
    }
    // a method to navigate to a URL
    public void navigateTo(String url) {
        driver.get(url);
    }

    // Properly close the WebDriver instance
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}


public class Main {
    public static void main(String[] args) throws Exception {
        Page page = new Page();
        page.navigateTo("http://www.w3schools.com/html/html_tables.asp");
        Thread.sleep(100);
        WebElement table = page.driver.findElement(By.xpath("//*[@id='customers']"));
        try {
            CSVReader reader = new CSVReader(new FileReader("extarnalFile\\externalFile.csv"));
            String data[];
            while ((data = reader.readNext()) != null) {
                // Ensure that the row has at least four columns
                if (data.length >= 4) {
                    // Extract values from each column
                    int searchIndex = Integer.parseInt(data[0]);
                    String searchContent =data[1];
                    int returnIndex = Integer.parseInt(data[2]);
                    String expectedText =data[3];
                    System.out.println("Test Parameters are:\n Search index: " +searchIndex + ", Search content: "
                            + searchContent + ", returnIndex: " + returnIndex+ ", expectedText: " + expectedText);
                    System.out.println(verifyTableCellText(table,searchIndex, searchContent, returnIndex, expectedText));
                    getTableCellTextByXpath(table,searchIndex, searchContent, returnIndex);
                } else {
                    //System.out.println("Row does not have at least three columns");
                }
            }
            // Close the CSVReader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

    public static boolean verifyTableCellText(WebElement table, int searchColumn, String searchText, int returnColumnText, String expectedText) throws Exception {
        String actual_result = getTableCellTextByXpath(table, searchColumn, searchText, returnColumnText);
        System.out.println("actual_result: "+actual_result +" expectedText: "+expectedText);
        return actual_result.equals(expectedText);
    }
    public static String getTableCellTextByXpath(WebElement table,int searchColumn, String searchText, int returnColumnText) throws Exception {
       WebElement actual_result=table.findElement(By.xpath("//*[@id='customers']/tbody/tr[td[contains(text(),'"+ searchText+"')]]/td["+returnColumnText+"]"));
       return actual_result.getText();

    }
}