# OpenAPI Specification to Java Client SDK Generator

---

This project intends to help you generate an API Wrapper that fits around a given API Specification, preferably Swagger/OpenAPI Specification and generate a module to be used in other projects in order to interact with the API.

---
## Benefits of using an API Wrapper

When programming your business logic, you won't love to handle extra complexity of the retry mechanisms and code corrections and the logic that is required to be used in the project that you're writing your business logic in. Well other than this, there can be a lot of factors that you need to take care of in order to ensure that the response data is being correctly parsed.

---
## Installation Steps

Well currently, you can fork the repo, clone it and specify the file which contains the specification for the API. Make sure the file is named `openapi.yaml` in order for the project to pick up the file. Then simply run the `APIWrapperGenerator` class in order to get the wrapper generated. 