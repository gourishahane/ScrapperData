package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation.WithImplicitInvocationTargetType;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @Test(enabled = true)
    public void testCase01() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            String url = "https://www.scrapethissite.com/pages/";
            if (!driver.getCurrentUrl().equals(url)) {
                driver.get(url);
                System.out.println("User is navigated to Scraper site");
            }

            wait.until(ExpectedConditions.urlContains("scrapethissite"));
            System.out.println("url contains 'scrapethissite'");

            WebElement hockeyLink = driver.findElement(By.xpath("//a[@href='/pages/forms/']"));
            Wrappers.clickOnElement(driver, hockeyLink);
            wait.until(ExpectedConditions.urlContains("forms"));
            System.out.println("User is navigated to 'Hockey Teams: Forms, Searching and Pagination' page.");


        ArrayList<HashMap<String, Object>> teamDataList = new ArrayList<>();
        int currentPage = 1;
        int maxPages = 4;
        while (currentPage <= maxPages) {
            List<WebElement> tableData = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//tr[@class='team']")));

            for (WebElement tableRow : tableData) {
                tableData = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//tr[@class='team']")));

                WebElement winPercent = tableRow.findElement(By.xpath(".//td[@class='pct text-success'] | .//td[@class='pct text-danger']"));
                String winPercentText = winPercent.getText();
                Float percent = Float.parseFloat(winPercentText);

                WebElement teamName = tableRow.findElement(By.xpath("./td[@class='name']"));
                String teamNameText = teamName.getText();

                WebElement year = tableRow.findElement(By.xpath("./td[@class='year']"));
                String yearText = year.getText();

                if (percent < 0.40) {
                    // Creating a HashMap for each team's data
                    HashMap<String, Object> teamData = new HashMap<>();
                    teamData.put("epoch_time", System.currentTimeMillis()); // Adding the epoch time of scrape
                    teamData.put("team_name", teamNameText);
                    teamData.put("year", yearText);
                    teamData.put("win_percentage", percent);
                    
                    teamDataList.add(teamData); // Adding the team's data to the list
                    
                    System.out.println("Team Name: " + teamNameText + ", Year: " + yearText + ", Percent: " + percent);
                }
            }
            
            if (currentPage < maxPages) {
                try {
                    WebElement nextPageLink = driver.findElement(By.xpath("//a[@aria-label='Next']"));
                    Wrappers.clickOnElement(driver, nextPageLink);  
                    wait.until(ExpectedConditions.stalenessOf(tableData.get(0)));  // Wait for the page to reload
                } catch (NoSuchElementException e) {
                    // If there is no "Next" button, exit the loop
                    System.out.println("No more pages available.");
                    break;
                }
            }
            currentPage++;
        }

        // Convert the ArrayList of HashMaps to JSON using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("hockey-team-data.json");
        objectMapper.writeValue(jsonFile, teamDataList);

        System.out.println("Data has been written to hockey-team-data.json");

        } catch (Exception e) {
            System.out.println("Exception occurred in testCase01 :" + e.getMessage());
            e.printStackTrace();
        }
    }
    @Test
    public void testCase02() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            String url = "https://www.scrapethissite.com/pages/";
            if (!driver.getCurrentUrl().equals(url)) {
                driver.get(url);
                System.out.println("User is navigated to Scraper site");
            }

            wait.until(ExpectedConditions.urlContains("scrapethissite"));
            System.out.println("url contains 'scrapethissite'");

            WebElement hockeyLink = driver.findElement(By.xpath("//a[contains(text(),'Oscar Winning Films:')]"));
            Wrappers.clickOnElement(driver, hockeyLink);
            wait.until(ExpectedConditions.urlContains("ajax-javascript"));
            System.out.println("User is navigated to 'Oscar Winning Films: AJAX and Javascript' page.");
            
            ArrayList<HashMap<String, Object>> movieDataList = new ArrayList<>();
            List<WebElement> yearElements=driver.findElements(By.xpath("//div[@class='col-md-12 text-center']/a"));
            String scrapingYear;
            for(WebElement yearElement:yearElements){
                Wrappers.clickOnElement(driver, yearElement);
                List<WebElement> movies=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//tr[@class='film']")));
                
                int movieCount=0;
                scrapingYear = yearElement.getText();
                System.out.println("Scraping data for year: " + scrapingYear);
                
                for(WebElement movie:movies){
                    WebElement movieTitle=movie.findElement(By.xpath("./td[@class='film-title']"));
                    String movieText=movieTitle.getText();

                    WebElement nomination=movie.findElement(By.xpath("./td[@class='film-nominations']"));
                    String nominationText=nomination.getText();

                    WebElement awards=movie.findElement(By.xpath("./td[@class='film-awards']"));
                    String awardsText=awards.getText();

                    if(movieCount==5) break; //to get top 5 movies
                    List<WebElement> bestPicturesElements=movie.findElements(By.xpath(".//td[@class='film-best-picture']"));
                    boolean isWinner=false;
                    if (!bestPicturesElements.isEmpty()) {
                        // Check if the element contains a flag indicating it's a winner
                        List<WebElement> winFlagElements = bestPicturesElements.get(0).findElements(By.xpath(".//i[contains(@class,'glyphicon-flag')]"));
                        if (!winFlagElements.isEmpty()) {
                            isWinner = true; // Set to true if winner flag is present
                            movieText += " (Winner)";
                        }
                    }
                    HashMap<String, Object> movieData = new HashMap<>();
                    movieData.put("EpochTime", System.currentTimeMillis());
                    movieData.put("Year", scrapingYear);
                    movieData.put("Title", movieText);
                    movieData.put("Nomination", nominationText);
                    movieData.put("Awards", awardsText);
                    movieData.put("isWinner", isWinner);
            
                    movieDataList.add(movieData);
                    movieCount++;
                   
                }
                // Print the movies for the current year
                   System.out.println("Top 5 movies for year " + scrapingYear + ": " + movieDataList);
                   movieDataList.clear(); // Clear the list for the next year

                   yearElements=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='col-md-12 text-center']/a")));
            }
                 File outputDir = new File("C:\\Users\\Gouri Shahane\\gourishahane-ME_QA_XSCRAPE_DATA\\output");
                 if (!outputDir.exists()) {
                 outputDir.mkdirs(); // Create the output directory if it doesn't exist
                 }
            
                 ObjectMapper mapper = new ObjectMapper();
                 mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print
    
             try {
                     // Write the list of movie data to a JSON file
                  String filePath = "C:\\Users\\Gouri Shahane\\gourishahane-ME_QA_XSCRAPE_DATA\\output\\oscar-winner-data.json";
                  mapper.writeValue(new File(filePath), movieDataList);
                  System.out.println("Data successfully written to oscar-winner-data.json");

                  File jsonFile = new File(filePath);
                  Assert.assertTrue(jsonFile.exists(), "JSON file should be present");
                  Assert.assertTrue(jsonFile.length() > 0, "JSON file should not be empty");
                  } catch (IOException e) {
                   e.printStackTrace();
                  }
         } catch (Exception e) {
            System.out.println("Exception occurred in testCase02 :" + e.getMessage());
            e.printStackTrace();
        }
    }
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}