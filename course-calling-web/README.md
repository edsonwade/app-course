```bash
  cd event $ ./mvnw package # or mvn clean package
  docker build -t event-service:0.0.1-SNAPSHOT .
````

```bash
  cd registration $ ./mvnw package # or mvn clean package
  docker build -t registration-service:0.0.1-SNAPSHOT .
````