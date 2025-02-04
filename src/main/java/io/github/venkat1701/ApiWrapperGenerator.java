package io.github.venkat1701;

import com.squareup.javapoet.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class ApiWrapperGenerator {
    public static void generateApiWrapper(OpenAPI openAPI, String outputDir) throws IOException {
        // yeh voh class hai jismei saare code jayenge and last mei flush hoga.
        TypeSpec.Builder apiWrapperClassBuilder = TypeSpec.classBuilder("ApiWrapper")
                .addModifiers(Modifier.PUBLIC);
        openAPI.getPaths().forEach((path, pathItem) -> {
            Map<PathItem.HttpMethod, Operation> operations = pathItem.readOperationsMap();
            operations.forEach((httpMethod, operation) -> {
                String methodName = operation.getOperationId();
                if (methodName == null || methodName.isEmpty()) {
                    methodName = httpMethod.name().toLowerCase() + "_" + path;
                }
                // basically this will create a separate method for each openapi spec path
                // and then handle request-response gracefully with exc handling

                //using code block was important and I just made use of jep 378 to write efficient and more readable code.
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(sanitizeMethodName(methodName))
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addParameter(String.class, "jsonRequestBody")
                        .addCode("""
                            OkHttpClient client = new OkHttpClient();
                            RequestBody body = RequestBody.create(jsonRequestBody, MediaType.get("application/json"));
                            Request request = new Request.Builder()
                                .url("$L")
                                .method("$L", body)
                                .build();
                            try (Response response = client.newCall(request).execute()) {
                                return response.body() != null ? response.body().string() : null;
                            } catch (IOException e) {
                                System.err.println("Network error occurred: " + e.getMessage());
                                return "Network error";
                            } catch (Exception e) {
                                System.err.println("Unexpected error: " + e.getMessage());
                                return "Unexpected error";
                            }
                        """, path, httpMethod.name())
                        .addException(IOException.class);
                apiWrapperClassBuilder.addMethod(methodBuilder.build());
            });
        });
        TypeSpec apiWrapperClass = apiWrapperClassBuilder.build();
        JavaFile javaFile = JavaFile.builder("io.github.venkat1701.api", apiWrapperClass)
                .build();
        javaFile.writeTo(Paths.get(outputDir));
    }

    private static String sanitizeMethodName(String methodName) {
        return methodName.replaceAll("[^a-zA-Z0-9]", "_");
    }

    public static void main(String[] args) {
        try {
            String specPath = "src/main/resources/openapi.yaml";
            OpenAPI openAPI = new OpenAPIV3Parser().read(specPath);
            if (openAPI == null) {
                System.err.println("Failed to parse the OpenAPI specification.");
                return;
            }
            generateApiWrapper(openAPI, "./generated-sources");
            System.out.println("API wrapper generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
