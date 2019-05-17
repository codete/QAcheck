## Regression testing platform

Platform contains four modules:
- api - library which is used by a developer to run screenshot comparison in tests,
- client - graphical user interface for platform,
- server - core of our platform, responsible for saving screenshots and doing comparisons.
- examples - example tests which use api library.

#### What do you need to run a platform
1. Java 11.
2. Maven.
3. [Chrome driver](https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver) - to run selenium tests.
4. [nodejs](https://nodejs.org/en/download/) - to run client module.

#### How to run 
1. Build whole platform with ```mvn clean install```.
2. Run server - please read [server readme](server/README.md).
3. Run client - please read [client readme](client/README.md).

To test if everything is working properly you can run one of our [example tests](examples). After running test you should be able to see test result on GUI (`http://localhost:4200/`).

#### Documentation
- [Step by step guide how to run your first test](documentation/easyStart.md)
- [Cloud deployment](documentation/cloud.md)
- [Tests engine](documentation/testEngine.md)
- [Architecture](documentation/architecture.md)