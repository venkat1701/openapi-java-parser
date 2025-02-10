package io.github.venkat1701.build.utils.okhttp;

import com.squareup.javapoet.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * This ApiWrapperGenerator is responsible for generating the Client SDK with Okhttp.
 * @author Venkat
 */
public class OkHttpApiClientGenerator {
    public static void generateApiWrapper(OpenAPI openAPI, String outputDir) throws IOException {
        TypeSpec.Builder apiWrapperClassBuilder = TypeSpec.classBuilder("ApiWrapper")
                .addModifiers(Modifier.PUBLIC);
        openAPI.getPaths().forEach((path, pathItem) -> {
            Map<PathItem.HttpMethod, Operation> operations = pathItem.readOperationsMap();
            operations.forEach((httpMethod, operation) -> {
                String methodName = operation.getOperationId();
                if (methodName == null || methodName.isEmpty()) {
                    methodName = httpMethod.name().toLowerCase() + "_" + path;
                }

                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(sanitizeMethodName(methodName))
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addParameter(String.class, "jsonRequestBody")
                        .addException(IOException.class);
                CodeBlock methodBody = CodeBlock.builder()
                        .addStatement("$T client = new $T()", ClassName.get("okhttp3", "OkHttpClient"), ClassName.get("okhttp3", "OkHttpClient"))
                        .addStatement("$T body = $T.create(jsonRequestBody, $T.get(\"application/json\"))",
                                ClassName.get("okhttp3", "RequestBody"),
                                ClassName.get("okhttp3", "RequestBody"),
                                ClassName.get("okhttp3", "MediaType"))
                        .addStatement("$T request = new $T.Builder()"
                                        + ".url($S)"
                                        + ".method($S, body)"
                                        + ".build()",
                                ClassName.get("okhttp3", "Request"),
                                ClassName.get("okhttp3", "Request"),
                                path,
                                httpMethod.name())
                        .beginControlFlow("try ($T response = client.newCall(request).execute())",
                                ClassName.get("okhttp3", "Response"))
                        .addStatement("return response.body() != null ? response.body().string() : null")
                        .endControlFlow()
                        .beginControlFlow("catch ($T e)", IOException.class)
                        .addStatement("$T.err.println(\"Network error occurred: \" + e.getMessage())", System.class)
                        .addStatement("return \"Network error\"")
                        .endControlFlow()
                        .beginControlFlow("catch ($T e)", Exception.class)
                        .addStatement("$T.err.println(\"Unexpected error: \" + e.getMessage())", System.class)
                        .addStatement("return \"Unexpected error\"")
                        .endControlFlow()
                        .build();

                methodBuilder.addCode(methodBody);
                apiWrapperClassBuilder.addMethod(methodBuilder.build());
            });
        });
        TypeSpec apiWrapperClass = apiWrapperClassBuilder.build();
        JavaFile javaFile = JavaFile.builder("com.example.api", apiWrapperClass)
                .build();
        javaFile.writeTo(Paths.get(outputDir));
    }

    private static String sanitizeMethodName(String methodName) {
        return methodName.replaceAll("[^a-zA-Z0-9]", "_");
    }

}
