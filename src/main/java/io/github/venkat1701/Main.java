package io.github.venkat1701;

import io.github.venkat1701.openapi.parser.YAMLParser;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.core.models.ParseOptions;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        var parser = new YAMLParser("src/main/resources/openapi.yaml");
        var parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        var result = parser.readLocationWithAuthValue(null, parseOptions);
        var openAPI = parser.getOpenAPI();

        // This thing below lists down all the operations required to create the SDK.
//        openAPI.getPaths().forEach((path, pathItem) -> {
//            Map<PathItem.HttpMethod, Operation> operations = pathItem.readOperationsMap();
//            operations.forEach((method, operation) -> System.out.println("Method: "+method + "\nOperation: "+operation));
//        });
        demoGenerateSources(parser);
    }

    public static void demoGenerateSources(YAMLParser parser) throws IOException {
        var openApi = parser.getOpenAPI();

    }
}