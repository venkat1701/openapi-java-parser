package io.github.venkat1701.build.utils.retrofit;

import com.squareup.javapoet.*;
import io.swagger.v3.oas.models.OpenAPI;

import javax.lang.model.element.Modifier;
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
                .addModifiers(Modifier.PUBLIC);

        openAPI.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                apiInterface.addMethod(
                        MethodSpec.methodBuilder(operation.getOperationId())
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .addAnnotation(
                                        AnnotationSpec.builder(
                                                        ClassName.get("retrofit2.http", httpMethod.name()))
                                                .addMember("value", "$S", path)
                                                .build())
                                .returns(ClassName.get("retrofit2", "Call"))
                                .build());
            });
        });

        JavaFile javaFile = JavaFile.builder("com.example.api", apiInterface.build()).build();
        javaFile.writeTo(Paths.get(outputDir + "/src/main/java"));
    }
}
