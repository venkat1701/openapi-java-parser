# OpenAPI Specification to Java Client SDK Generator

This project helps generate an API Wrapper (SDK) for any API based on its OpenAPI Specification. The generated module provides a structured and easy-to-use Java client for interacting with the API, reducing the need to manually handle HTTP requests, responses, and error handling.

## Features

- **Automatic API Client Generation** – Converts an OpenAPI Specification into a ready-to-use Java SDK.
- **Supports OkHttp & Retrofit** – Choose between OkHttp (low-level control) or Retrofit (high-level API client).
- **Handles Request & Response Parsing** – Uses Gson for automatic JSON serialization.
- **Customizable Dependencies** – Select dependencies dynamically via CLI.
- **Improved API Integration** – No need to manually construct API requests.

## Why Use an API Wrapper?

When developing applications, directly handling HTTP requests, retries, response parsing, and API errors can lead to unnecessary complexity. By using a generated API Wrapper (SDK):

- API requests and responses are managed within a structured Java client.
- Business logic remains separate from network-related concerns.
- Handling retries, authentication, and error handling becomes standardized.

## Installation

### Prerequisites

- Java 17 or later
- Maven
- OpenAPI YAML file (named `openapi.yaml`)

### Build and Install

1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/openapi-java-parser.git
   cd openapi-java-parser
   ```
2. Build the Project:
   ```shell
   mvn clean package
   ```
3. The compiled JAR will be available at: 
   ```
   target/openapi-java-parser-{latest-version}.jar
   ```
## Usage
### Video Link
You can visit our youtube channel to check the working of it: [https://youtu.be/nEoP3hjDhIQ](Youtube)
### Running the CLI Tool
To generate an API wrapper, run the JAR with the following options:

```sh
java -jar target/openapi-java-parser-{latest-release-version}.jar
-spec path/to/openapi.yaml 
-output path/to/output_directory
-dependencies okhttp,jackson,slf4j
-type okhttp
```

### CLI Options

| Option | Description |
|--------|-------------|
| `-s, --spec` | Path to the OpenAPI YAML file |
| `-o, --output` | Output directory for the generated SDK |
| `-d, --dependencies` | Comma-separated list of dependencies (e.g., `retrofit,jackson,okhttp`) |
| `-t, --type` | Type of API wrapper (`retrofit` or `okhttp`) |

## Verifying the Generated SDK
Once the SDK is generated, navigate to the output directory:
```shell
ls output
```
It should contain:
```textmate
generated-sdk
├── pom.xml
├── src/main/java/com/example/
│   ├── MainApplication.java
│   ├── api/
│   │   ├── ApiWrapper.java
│   │   ├── RetrofitAPIClient.java (For Retrofit)
│   │   ├── OkHttpAPIClient.java (For OkHttp)
```

## Running the Generated SDK
1. Navigate to SDK Directory:
   ```shell
   cd generated-sdk
   ```
2. Build the SDK
   ```shell
   mvn clean package
   ```
3. Run the SDK:
   ```shell
   mvn exec:java -Dexec.mainClass="com.example.MainApplication"
   ```
## Contributing
Contributions are welcome. If you would like to contribute, follow these steps:

1. Fork the repository
2. Create a new branch (feature/new-feature)
3. Commit your changes (git commit -m 'Add new feature')
4. Push to the branch (git push origin feature/new-feature)
5. Open a pull request

## Developer Contacts
 - Email: krishjaiswal1701@gmail.com
 - Linkedin: https://linkedin.com/in/jaiswal-krish