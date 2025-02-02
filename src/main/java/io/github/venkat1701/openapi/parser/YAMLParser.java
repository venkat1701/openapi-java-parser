package io.github.venkat1701.openapi.parser;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.AuthorizationValue;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.util.List;

public class YAMLParser {
    private String location;
    private OpenAPI openAPI;

    public YAMLParser(String location) {
        if(location.isEmpty()) {
            throw new RuntimeException("YAML location is empty");
        } else if(location.isBlank()) {
            throw new RuntimeException("YAML location is blank");
        } else {
            this.location = location;
            openAPI = new OpenAPIV3Parser().read(this.location);
        }
    }

    public SwaggerParseResult readLocationWithAuthValue(List<AuthorizationValue> authValue, ParseOptions parseOptions) {
        var result = new OpenAPIV3Parser().readLocation(this.location, authValue, parseOptions);
        this.openAPI = result.getOpenAPI();
        result.getMessages().forEach(System.out::println);
        return result;
    }

    public OpenAPI getOpenAPI() {
        return this.openAPI;
    }




}
