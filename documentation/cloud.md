#### Cloud server requirements
- java (version 11),
- http server to serve client files (index.html, *.js, etc.).

#### Paths
All application files (server/client) are in the `/home/admin/regression-testing-platform` directory.

### Server deployment
Copy `.jar` file from `server/target` directory to AWS instance. You can run server using command:
`nohup java -jar -Dspring.profiles.active=prod  $jar_file > output.log  2>&1 &`

### Client deployment
Copy all files from `client/dist/regression-testing-platform` directory to your AWS server.


To run client you need http server which will host your files (*.js, index.html, etc.). You can use for example _Apache server_.

##### Configuration of the Apache server
All Angular routes should be served via the index.html file. This can be achieved by adding a `.htaccess` file (in the same directory where the index.html resides) with the following contents.

```
<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . index.html [L]
</IfModule>
```

Remember to edit your `/etc/apache2/sites-enabled/000-default.conf` file. 
You have to add following piece of code inside the VirtualHost block (assuming the Angular2 app is served from `/var/www/html`)
```
<Directory "/var/www/html">
    Options Indexes FollowSymLinks Includes ExecCGI
    AllowOverride All
    Require all granted
</Directory>
``` 

After above steps are done you have to enable `mod rewrite` using command:

```sudo a2enmod rewrite```

###### Apache server - proxy configuration
To properly handle calls from the client to the server you need to configure proxy. 

1. Please install proxy mods.
    ```
    sudo a2enmod proxy
    sudo a2enmod proxy_http
    ``` 
2. Add proxy configuration in `/etc/apache2/sites-enabled/000-default.conf` file (inside the VirtualHost block):
    ```
    ProxyPass /api http://localhost:8080
    ProxyPassreverse / http://localhost:8080/
    ```