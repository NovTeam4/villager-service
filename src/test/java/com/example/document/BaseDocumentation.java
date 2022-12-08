package com.example.document;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.Schema.schema;
import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public abstract class BaseDocumentation {

    private static final String DEFAULT_REST_DOC_PATH = "{class_name}/{method_name}/";

    protected RequestSpecification spec;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void setUpRestDocs(RestDocumentationContextProvider restDocumentation) {

        this.spec = new RequestSpecBuilder()
                .setPort(port)
                .addFilter(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    protected RequestSpecification givenAuthPass(String body, RestDocumentation restDocumentation) {
        return given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(body)
                .log().all()
                .filter(document(
                        restDocumentation.getIdentifier(),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(restDocumentation.getTag())
                                        .summary(restDocumentation.getSummary())
                                        .description(restDocumentation.getDescription())
                                        .requestSchema(schema(restDocumentation.getRequestSchema()))
                                        .responseSchema(schema(restDocumentation.getResponseSchema()))
                                        .requestFields(restDocumentation.getRequestFields())
                                        .pathParameters(restDocumentation.getPathParameters())
                                        .requestParameters(restDocumentation.getRequestParameters())
                                        .responseFields(restDocumentation.getResponseFields())
                                        .build()
                        )
                ));
    }

    protected RequestSpecification givenAuthRefreshToken(String body, RestDocumentation restDocumentation) {
        return given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(body)
                .log().all()
                .filter(document(
                        restDocumentation.getIdentifier(),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(restDocumentation.getTag())
                                        .summary(restDocumentation.getSummary())
                                        .description(restDocumentation.getDescription())
                                        .requestSchema(schema(restDocumentation.getRequestSchema()))
                                        .responseSchema(schema(restDocumentation.getResponseSchema()))
                                        .requestFields(restDocumentation.getRequestFields())
                                        .pathParameters(restDocumentation.getPathParameters())
                                        .requestParameters(restDocumentation.getRequestParameters())
                                        .responseFields(restDocumentation.getResponseFields())
                                        .requestHeaders(Arrays.asList(
                                                new HeaderDescriptorWithType(AUTHORIZATION).description("JWT Access-Token"),
                                                new HeaderDescriptorWithType("refresh-token").description("JWT Refresh-Token")
                                        ))
                                        .build()
                        )
                ));
    }

    protected RequestSpecification givenAuth(String body, RestDocumentation restDocumentation) {
        return given(this.spec)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .body(body)
                .log().all()
                .filter(document(
                        restDocumentation.getIdentifier(),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag(restDocumentation.getTag())
                                        .summary(restDocumentation.getSummary())
                                        .description(restDocumentation.getDescription())
                                        .requestSchema(schema(restDocumentation.getRequestSchema()))
                                        .responseSchema(schema(restDocumentation.getResponseSchema()))
                                        .requestFields(restDocumentation.getRequestFields())
                                        .pathParameters(restDocumentation.getPathParameters())
                                        .requestParameters(restDocumentation.getRequestParameters())
                                        .responseFields(restDocumentation.getResponseFields())
                                        .requestHeaders(List.of(
                                                new HeaderDescriptorWithType(AUTHORIZATION).description("JWT Access-Token")
                                        ))
                                        .build()
                        )
                ));
    }
}