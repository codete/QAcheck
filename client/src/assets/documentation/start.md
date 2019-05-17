#### Quick tutorial how to start using Regression Testing platform

#### Registration
1. Create new user: http://localhost:4200/
2. After successful registration you will receive API_KEY. Please save it, because you will need it to run API library.


![](./assets/documentation/images/api_key.png)
3. Please take a look into [our examples](/documentation/examples) how to use api library.
4. After running one of our examples please visit http://localhost:4200/ page and you will see all your tests.

#### API library dependency

##### Java
###### 1. Set proper credentials for Codete repository.
Create `~/.m2/settings.xml` file with following configuration:

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
      https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
      <server>
        <id>codete</id>
        <username>codete-maven-read-user</username>
        <password>4yCQxZK9hFjsVKRf</password>
      </server>
    </servers>
</settings>

```

######  2. Add ssl certificate for nexus.codete to trusted
1. Export https://nexus.codete certificate as `nexuscodete.cer` file (`.crt` extension is also fine).  
In a browser, navigate to [nexus.codete](https://nexus.codete) hit F12, go to certificates/security and get the top most certificate. Export it to `nexuscodete.cer` (base64 encoded). This process is different for each OS and Browser. 

2. Add this certificate for all jre/jdk that you are using (all java installations and IDE tools). Especially pay attention to IntelliJ, since it comes with its own version of jre.  
For example, if your IntelliJ was installed in `~/Software/Intelij`, then in order for it to trust ssl certificate for `nexus.codete` perform following actions:
    1. Navigate to `~/Software/Intelij/jre64/lib/security` folder. 
    2. Copy `nexuscodete.cer` into this folder.
    3. Execute following bash command:  `keytool -keystore cacerts -importcert -alias nexuscodete -file nexuscodete.cer`.  
        If keytool is not found, you're probably on windows so try `"%JAVA_HOME%/jre/bin/keytool" -keystore cacerts -importcert -alias nexuscodete -file nexuscodete.cer`.  
        Otherwise, locate your keytool in wherever you have java installed or use the one in `"jre(64|32)?" -> "bin"`.
    4. Use the default password of `changeit`.
    5. When prompted to trust the certificate type `yes`.
    6. Restart IDE.
    7. Repeat above steps for all java (including jdk) installations and jetbrains tools just to be safe. 
    7. If you are getting `Resource nexus-maven-repository-index.properties does not exist` error in IntelliJ's `Build tools > Maven > Repositories` section, delete `~/.m2/repository` folder.


###### 3. Add API library dependency to your pom.xml
```

<repositories>
    <repository>
        <id>codete</id>
        <url>https://nexus.codete/repository/maven-public/</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.codete.regression</groupId>
    <artifactId>regression-testing-platform-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```