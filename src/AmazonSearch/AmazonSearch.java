package AmazonSearch;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class AmazonSearch {

	public static void main(String[] args) {
		
		String Category = null;
		String SearchVal = null;

		
		/* milestone 1 - open www.amazon.in */
		
	
		System.setProperty("wedriver.chrome.driver", "chromedriver");
		WebDriver driver = new ChromeDriver();		
		
		driver.get("https://www.amazon.in");
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MICROSECONDS);		
		
		
		/* milestone 2 - connect to JDBC */
		try {

			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce1","root","root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Amazon");
			
			while (rs.next()) {
				System.out.println(rs.getString(2) + " " + rs.getString(3));
				Category = rs.getString(2);
				SearchVal = rs.getString(3);
			}

			/* milestone 3 - select category */
			WebElement weCat = driver.findElement(By.xpath("//select[@id='searchDropdownBox']"));
			
			Select slCatDrop = new Select(weCat);
			slCatDrop.selectByVisibleText(Category);

			WebElement weSearchText = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
			weSearchText.sendKeys(SearchVal);
			
			WebElement weSearchButton = driver.findElement(By.xpath("//input[@id='nav-search-submit-button']"));
			weSearchButton.click();
		
			/* milestone 4 - count search result */		
			List<WebElement> ResultCount = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']"));
			System.out.println("Total search result are: " + ResultCount.size());
			
			WebElement ResultBarText = driver.findElement(By.xpath("//*[@class='a-section a-spacing-small a-spacing-top-small']/span[1]"));
			System.out.println("Result bar: " + ResultBarText.getText());
			
			/* milestone 5 - capture screen */		
			// Note: remember to get the io jar https://mvnrepository.com/artifact/commons-io/commons-io/2.6
			TakesScreenshot TsObj = (TakesScreenshot) driver;
			
			File myFile = TsObj.getScreenshotAs(OutputType.FILE);
			
			try {
				FileUtils.copyFile(myFile, new File("amazonsearch.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		} catch (ClassNotFoundException e) {
				
			// e.printStackTrace();
			System.out.println("Class not found");
		}	catch (SQLException e) {
			
			//e.printStackTrace();
			System.out.println("SQL Exception");
				
		}

		
	

		// close after 5 seconds
		try {
			  Thread.sleep(5000); //time is in ms (1000 ms = 1 second)
			} catch (InterruptedException e) {e.printStackTrace();}			
		driver.close();
	
	
	}

	
	

}
