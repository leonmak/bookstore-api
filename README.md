# Bookstore API

## Run postgres container

- ensure username and password match `application.properties`
```shell
docker run --name pg1 -p 5432:5432 -e POSTGRES_USER=username -e POSTGRES_PASSWORD=password -e POSTGRES_DB=mydb -d postgres:15-alpine
```

## Run swagger UI

- Start API: `mvn spring-boot:run`
- Go to `http://localhost:8080/swagger-ui/index.html`
- Click authorize with default username and password `admin`, change in `application.properties`