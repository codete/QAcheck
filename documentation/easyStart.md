### Full guide how to run your first test 

#### Step 1: Install necessary software

* Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
* Install [Maven](https://maven.apache.org/)
    
    For Ubuntu:
    ```
    sudo apt update
    sudo apt install maven
    ```
    
#### Step 2: Build project

Inside project's root execute following command (note: you need to have maven installed):

```
mvn clean install
```

#### Step 3: Run server

* Create your custom profile for server application: `server/src/main/resources/application-dev.yaml`
    Here you can redefine following properties:
    
    ```
    screenshots:
      directory:
        path: "/path/to/screenshots"
    crawler:
      browserstack:
        username: ""
        key: ""
    spring:
      datasource:
        password: "" 
        
    ```
    
    `screenshots.directory.path` option is mandatory. Here you define where server will store screenshots.
    `crawler.browserstack.username` - username provided by browserstack. Optional parameter, define it if you want to use embedded crawler powered by browserstack (accessible via `+ New Tests button` ).
    `crawler.browserstack.key`- key provided by browserstack. Also an optional parameter

* After your configuration profile is ready you can run server app via either maven or IntelliJ. To run it via maven execute following command inside `server` folder:
    ```
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ``` 
    To run it from IntelliJ, execute it from `com.codete.regression.Application` class. Make sure to select correct Spring Boot profile by appending `VM options` with:
    ```
    -Dspring-boot.run.profiles=dev
    ``` 
    
#### Step 4: Run client
* Install [NodeJS and npm](https://nodejs.org/en/download/package-manager/)
* While server is running (step 3) execute following bash commands inside `client` folder
    ```
    npm install -g @angular/cli@latest
    npm install
    ng serve
    ```
* Navigate to `http://localhost:4200` in your browser. It will open client GUI
* Register your user. This will allow you to acquire `API key` needed to run tests. After registration your key will be available inside tab `Settings`.


#### Step 5: Configure api to use your account

* Inside class `com.codete.regression.examples.AccountConfig` modify `API_KEY` constant to store your key (acquired at the end of step 4)

#### Step 6: Install Selenium WebDriver

* [Download](https://sites.google.com/a/chromium.org/chromedriver/) driver for Chrome. Store it somewhere where you will be able to find it afterwards.

#### Step 7: Run some examples

* From IntelliJ run test `com.codete.regression.examples.browser.BrowserStaticPageTest`. You need to have selenium WebDriver available. Otherwise you will get an error while trying to execute test. Inside `VM options` add following:
    ```
    -Dwebdriver.chrome.driver="/path/to/selenium/chromedriver"
    ```
  
#### Step 8: See the results

* Access page `http://localhost:4200` and choose tab `Applications`. Here you will be able to see the results of your test run, executed on step 7