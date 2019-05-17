##### Running a server
1. Before running server set your **screenshots.directory.path** property in **application.yaml** file - all screenshots sent to the server will be stored in this directory.
2. If you wish to use embedded browserstack crawler available from the client, you need to provide your browserstack credentials (properties **crawler.browserstack.username** and **crawler.browserstack.key** in **application.yaml**)
3. Run server through **com.codete.regression.Application** class.

##### Properties
**screenshots.diff.color.threshold**. - used to configure color comparison sensitivity. Default value is 0, which means that even slightest change in pixel color counts towards total pixels changed.
Maximum value is 1024, at which point even black and white pixels would be considered equal.

