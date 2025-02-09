package io.github.venkat1701.build.utils.retrofit;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.models.OpenAPI;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Generates an API Client based on Retrofit
 *
 * @author Venkat
 */
public class RetrofitApiClientGenerator {

    public static void generateRetrofitApiWrapper(OpenAPI openAPI, String outputDir) throws IOException {
        TypeSpec.Builder apiInterface = TypeSpec.interfaceBuilder("RetrofitAPIClient")
                .addModifiers(javax.lang.model.element.Modifier.PUBLIC);

        openAPI.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                apiInterface.addMethod(
                        com.squareup.javapoet.MethodSpec.methodBuilder(operation.getOperationId())
                                .addModifiers(javax.lang.model.element.Modifier.PUBLIC, javax.lang.model.element.Modifier.ABSTRACT)
                                .addAnnotation(
                                        com.squareup.javapoet.AnnotationSpec.builder(
                                                        com.squareup.javapoet.ClassName.get("retrofit2.http", httpMethod.name()))
                                                .addMember("value", "$S", path)
                                                .build())
                                .returns(com.squareup.javapoet.ClassName.get("retrofit2", "Call"))
                                .build());
            });
        });

        JavaFile javaFile = JavaFile.builder("com.example.api", apiInterface.build()).build();
        javaFile.writeTo(Paths.get(outputDir + "/src/main/java"));
    }
}
