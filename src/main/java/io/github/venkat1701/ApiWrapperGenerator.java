package io.github.venkat1701;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
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
        TypeSpec.Builder apiWrapperClassBuilder = TypeSpec.classBuilder("ApiWrapperGenerator")
                .addModifiers(Modifier.PUBLIC);

        openAPI.getPaths().forEach((path, pathItem) -> {
            Map<PathItem.HttpMethod, Operation> operations = pathItem.readOperationsMap();
            operations.forEach((httpMethod, operation) -> {
                String methodName = operation.getOperationId();
                if (methodName == null || methodName.isEmpty()) {
                    methodName = httpMethod.name().toLowerCase() + "_" + path;
                }
                MethodSpec methodSpec = MethodSpec.methodBuilder(sanitizeMethodName(sanitizePath(methodName)))
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .addParameter(String.class, "request")
                        .addStatement("$T.out.println($S)", System.class,
                                "Executing " + httpMethod.name() + " on " + path)
                        .build();

                apiWrapperClassBuilder.addMethod(methodSpec);
            });
        });

        TypeSpec apiWrapperClass = apiWrapperClassBuilder.build();
        JavaFile javaFile = JavaFile.builder("io.demotesting.api", apiWrapperClass)
                .build();
        javaFile.writeTo(Paths.get(outputDir));
    }

    private static String sanitizePath(String path) {
        String[] sanitized = path.replaceAll("^/+", "").replaceAll("[^a-zA-Z0-9]", "_").split("_+");
        String methodName = sanitized[0];
        for(int i=1; i<sanitized.length; i++) {
            methodName += Character.toUpperCase(sanitized[i].charAt(0))+sanitized[i].substring(1);
        }
        return methodName;
    }

    private static String sanitizeMethodName(String methodName) {
        String[] methodNameArray = methodName.split("_");
        String sanitizedMethodName = methodNameArray[0];
        for(int i=1; i<methodNameArray.length; i++) {
            sanitizedMethodName += Character.toUpperCase(methodNameArray[i].charAt(0))+methodNameArray[i].substring(1);
        }
        return sanitizedMethodName;
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
