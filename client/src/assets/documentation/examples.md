In the [repository](http://gitlab.codete/codete-internal/regression-testing-platform/tree/develop/examples) (you have to be connected to the Codete network) you can find usage examples of our api library.

There are many ways to use our regression platform API:
- run selenium browser test,
- run appium test (mobile applications testing),
- run crawler through site map file,
- generate test case scenarios.

#### Selenium browser test
There are multiple examples:
- **BrowserStackTest** - testing page using BrowserStack integration, 
- **BrowserStaticPageTest** - testing page without dynamic elements,
- **BrowserDynamicPageTest** - testing page with dynamic elements. You have to possibilities to test such pages:
    - deleting all dynamic content from page, before taking screenshots. You have to implement filter which will delete such elements. Please take a look into our example filter - **CodeteDynamicElementsFilter**,
    - run comparator in _initialize_ignore_areas_ mode. Our comparator will try to find all dynamic elements and ignore them during comparison.

#### Appium mobile test
You can run screenshot comparison even for mobile applications. We integrated our api library with [appium](http://appium.io/).
To run appium test you will need:
- appium server,
- device emulator or real device.

Please take a look into [tutorial](https://www.swtestacademy.com/appium-tutorial/) how to integrate Android Virtual Device with Appium server.

There are two example tests:
- **AppiumCalculatorTest** - screenshot comparison for Android calculator app,
- **AppiumSettingsTest** - screenshot comparison for Android settings menu (we choose this menu, because it has vertical scroll).


#### Screenshot comparator settings
There is a possibility to configure few settings of screenshot comparator:
- take full page screenshot - when page has a vertical scroll you can invoke code ```screenshotComparator.setFullPageScreenshot(true)``` - api library will scroll through whole page and take multiple screenshots,
- set allowed difference threshold - using ``` screenshotComparator.setAllowedDifferencePercentage(double)``` method you can set minimum percentage difference below which test will fail. E.g. there is 5% difference between two screenshots, but `allowedDifferencePercentage=6` - it means that test will pass.
- ``` setAllowedDelta(allowedDelta) ``` - set maximum allowed difference between pixels colors in 4D space (RGBA if perceptual mode is disabled or CIELAB otherwise) for comparator to treat them as same color (0.0 by default)
- ``` setPerceptualMode(true) ``` - convert colors to CIELAB color space before performing delta calculation (false by default)
- ``` setHorizontalShift(horizontalShift) ``` - width of scanning window for anti-aliasing detection. Can make impact on performance, so try not to set large (> 4) values here. (0 by default)
- ``` setVerticalShift(verticalShift) ``` - height of scanning window for anti-aliasing detection. Same as horizontal shift, can make impact on performance, so try not to set large (> 4) values here. (0 by default)
- ``` setShowDetectedShift(true)``` - draw detected shift (anti-aliasing) on diff image (false by default)

#### Crawler through a site map file
Please take a look into **CrawlerTest**. This test uses api library to crawl through site map file and take screenshots of each page in the site map.
You can run crawler in two modes:
- with dynamic areas detection - api library will take multiple screenshots of each page, detect dynamic elements and add ignore areas for such elements.
- without dynamic areas detection - api library will take one screenshot of each page and won't detect dynamic elements.

#### Generate test case scenarios
You can use our library to automatically search for test case scenarios on your web page. Library will go through your page and generate tests for all elements which can change page state (e.g. hover/click elements.)
Take a look into `SeleniumTestGenerator` to check how you can configure `TestCaseGenerator`.  

Settings which you can configure:
- `setApiKey(string)` - your personal apiKey which is connected with your account.
- `setAppName(string)` - this name will be saved in java test file and will be visible on `Regression testing platform GUI` when you run such test.
- `setGeneratedTestDirectoryPath(string)` - directory where tests will be saved.
- `setScreenshotsDirectoryPath(string)` - directory where screenshots taken during test generation will be saved. For earch generated test you will have three screenshots: before action, after action and screenshot with diff between before-after.
- `setPageUrl(string)` - main page. Generator will generate test for this page. If you use `setRunWithCrawler(true)` crawler will search for main page sunbpages.
- `setTestPackage(string)` - java package of generated tests.
- `setClassNamePrefix(string)` - prefix which will be added to each generated java class.
- `setBeforeEach(string)` - code which will be added in the `@BeforeEach` block.
- `setCustomCode(string)` - code which will be added at the end of the generated test. For example: you can add here a method, which you will invoke in the `@BeforeEach` block. 

You can run generator in `crawler` mode:
```$xslt
testCaseGeneratorRequest.setRunWithCrawler(true);
testCaseGeneratorRequest.setCrawlerMaxPagesToVisit(2);
```
it indicates that our crawler will search subpages and generate tests not only for the main page but also for all subpages.

