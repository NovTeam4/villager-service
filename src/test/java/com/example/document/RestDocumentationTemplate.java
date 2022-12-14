package com.example.document;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import com.example.villagerservice.common.exception.ErrorResponse;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import java.util.ArrayList;
import java.util.List;

public class RestDocumentationTemplate extends BaseRestDocumentationTemplate {
    private final String tag;

    public RestDocumentationTemplate(String tag) {
        this.tag = tag;
    }

    public RestDocumentation requestRestDocumentation(String summary,
                                                      String errorKey,
                                                      String errorDescription,
                                                      List<FieldDescriptor> requestFieldDescriptor,
                                                      String requestSimpleName) {
        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                requestSimpleName,
                ErrorResponse.class.getName(),
                requestFieldDescriptor,
                new ArrayList<>(),
                new ArrayList<>(),
                getErrorResponseAuthFields(errorKey, errorDescription));
    }

    public RestDocumentation requestRestDocumentation(String summary,
                                                      List<FieldDescriptor> requestFieldDescriptor,
                                                      String requestSimpleName) {

        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                requestSimpleName,
                "",
                requestFieldDescriptor,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }

    public RestDocumentation requestRestPartsDocumentation(String summary,
                                                      List<RequestPartDescriptor> requestParts,
                                                      String requestSimpleName) {

        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                requestSimpleName,
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                requestParts,
                new ArrayList<>(),
                new ArrayList<>());
    }

    public RestDocumentation requestRestDocumentation(String summary,
                                                      List<FieldDescriptor> requestFieldDescriptor,
                                                      String requestSimpleName,
                                                      List<ParameterDescriptorWithType> pathParameters) {

        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                requestSimpleName,
                "",
                requestFieldDescriptor,
                new ArrayList<>(),
                pathParameters,
                new ArrayList<>());
    }

    public RestDocumentation requestRestDocumentation(String summary,
                                                      List<ParameterDescriptorWithType> pathParameters) {

        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                pathParameters,
                new ArrayList<>());
    }

    public RestDocumentation requestRestDocumentation(String summary) {

        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }

    public RestDocumentation responseRestDocumentation(String summary,
                                                       List<FieldDescriptor> responseFieldDescriptor,
                                                       String responseSimpleName) {
        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                "",
                responseSimpleName,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                responseFieldDescriptor);
    }

    public RestDocumentation responseRestDocumentation(String summary,
                                                       List<FieldDescriptor> responseFieldDescriptor,
                                                       String responseSimpleName,
                                                       List<ParameterDescriptorWithType> pathParameters) {
        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                "",
                responseSimpleName,
                new ArrayList<>(),
                new ArrayList<>(),
                pathParameters,
                responseFieldDescriptor);
    }

    public RestDocumentation allRestDocumentation(String summary,
                                                  List<ParameterDescriptorWithType> requestParameters,
                                                  String requestSchema,
                                                  List<FieldDescriptor> responseFieldDescriptor,
                                                  String responseSimpleName
    ) {
        String identifier = summary;
        return createRestDocumentation(
                tag,
                summary,
                identifier,
                "",
                requestSchema,
                responseSimpleName,
                new ArrayList<>(),
                requestParameters,
                new ArrayList<>(),
                responseFieldDescriptor);
    }

    public RestDocumentation allRestDocumentation(String summary,
                List<ParameterDescriptorWithType> pathParameters,
                List<FieldDescriptor> responseFieldDescriptor,
                String responseSimpleName
    ) {
        String identifier = summary;
        return createRestDocumentation(
            tag,
            summary,
            identifier,
            "",
            "",
            responseSimpleName,
            new ArrayList<>(),
            new ArrayList<>(),
            pathParameters,
            responseFieldDescriptor);
    }

    public RestDocumentation responseRestDocumentation(String summary,
        List<ParameterDescriptorWithType> requestParameters,
        List<ParameterDescriptorWithType> pathParameters,
        List<FieldDescriptor> responseFieldDescriptor,
        String responseSimpleName
    ) {
        String identifier = summary;
        return createRestDocumentation(
            tag,
            summary,
            identifier,
            "",
            "",
            responseSimpleName,
            new ArrayList<>(),
            requestParameters,
            pathParameters,
            responseFieldDescriptor);
    }
}
