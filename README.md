# IDC Dashboard REST API Dev
This is the deployment guide for the IDC Dashboard REST API API.

## Prerequisites
1. Java 8
2. Maven 3+
3. PostgreSQL 9.4+


## PostgreSQL Setup
- Create database with `sqls/db.sql`.
- Create tables with `sqls/ddl.sql`.
- If you want to drop all tables please run `sqls/drop.sql`.
- If you want to clean all tables please run `sqls/clear.sql`.
- If you want to prepare test data please run `sqls/testdata.sql`.
- (Optional) If you want to add indices (for better performance) to the tables, run `sqls/index.sql`   

## API Configuration
### application configuration
Edit file `src/main/resources/application.properties`:  

- **spring.datasource.url**: PostgreSQL server connection url  
- **spring.datasource.username**: PostgreSQL Server username  
- **spring.datasource.password**: PostgreSQL Server password  
- **server.port**: the server port on which the API will run on   

You can keep the rest of the parameters unchanged.

### log4j configuration
Edit file `src/main/resources/log4j.properties`:  

- **log4j.logger.com.ge.predix.labs**: the log level to be used


## Deployment
You can run below mvn command to run application directly.  
set the env variables or modify the config in `src/main/resources/application.properties`  
for example, in linux or mac:

```bash
export DB_URL=jdbc:postgresql://127.0.0.1:5432/idc
export DB_USERNAME=postgres
export DB_PASSWORD=123456
export DB_POOLSIZE=128
export PORT=8183
```

Then run:

``` bash
mvn clean spring-boot:run
```


## Swagger
Open **http://editor.swagger.io/** and copy  `docs/swagger.yaml` to verify.

## Verification
Prepare clean and test data in db with `sqls/clear.sql` and `sqls/testdata.sql`.
Import Postman collection `docs/postman.json` with environment variables `docs/postman-env.json`. 

For failure tests in Postman, you can check `docs/postman-failure.json`


