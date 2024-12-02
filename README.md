# Automated Data Scraping for Hockey Teams and Oscar-Winning Films 

## Project Title
Automated Data Scraping and Curation for Sports and Entertainment

## Description
This project automates the extraction and curation of data from two sources:
1. **Hockey Teams:** Scraping multiple pages of hockey team data, calculating win rates below 40%, and storing the results in a JSON file.
2. **Oscar-Winning Films:** Scraping Oscar-winning films for multiple years, collecting details like nominations, awards, and determining whether they won the "Best Picture" award.

### Key Features
- Extracts hockey team data from four pages and stores it in JSON format with fields such as team name, year, win percentage, and epoch time of scrape.
- Scrapes Oscar-winning films, including the title, nominations, awards, winner status, and year of data scrape.
- Outputs the curated data as JSON files in a structured format.
- Verifies the existence and content of the generated JSON files using TestNG assertions.
- Uses wrapper methods to ensure robust handling of Selenium interactions.
- Includes mechanisms to handle CAPTCHA requirements during automation.

## Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/gourishahane/ScrapperData.git

2. Navigate to the project directory:
   cd Automated-Data-Scraping
   
3. Build the project using Gradle:
   gradle build
   
4. Set up the required browser drivers (e.g., ChromeDriver) and ensure they are in the system path.

5. Execute tests using TestNG:
   gradle test

## Usage
- Update the config.properties file with the necessary URLs and settings for scraping.
- Run specific test cases or the entire suite from the terminal or your preferred IDE.
- JSON files will be saved in the output directory in the project root.

## Dependencies
- Selenium WebDriver
- Jackson Library for JSON serialization
- TestNG for testing and assertions
- Gradle as the build tool


## Contact Information
- Name: Gouri Shahane
- Email: gouri.shahane@example.com
- GitHub: github.com/gourishahane
- LinkedIn: linkedin.com/in/gourishahane










