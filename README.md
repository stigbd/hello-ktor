# hello-ktor

This is a simple Ktor project that demonstrates how to create a simple REST API using [Ktor](https://ktor.io/).

## Building the application

```zsh
% ./gradlew build -t
```

## Running tests with coverage reporting

```zsh
% ./gradlew clean koverHtmlReport koverVerify
```
## Running the application

In another terminal, run the following command:

```zsh
% ./gradlew run
```

## Testing the application

You can test the application by running the following command:

```zsh
% curl http://localhost:8080
```
