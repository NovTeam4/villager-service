package com.example.document;

import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import org.jetbrains.annotations.NotNull;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.Arrays;
import java.util.List;

import static com.example.villagerservice.common.exception.CommonErrorCode.DATA_INVALID_ERROR;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public abstract class BaseRestDocumentationTemplate {
    protected RestDocumentation createRestDocumentation(String tag, String summary, String identifier, String description,
                                                        String requestSchema, String responseSchema,
                                                        List<FieldDescriptor> requestFieldsSnippet,
                                                        List<ParameterDescriptorWithType> requestParameters,
                                                        List<ParameterDescriptorWithType> pathParameters,
                                                        List<FieldDescriptor> responseFieldsSnippet) {
        return RestDocumentation.builder()
                .tag(tag)
                .summary(summary)
                .identifier(identifier)
                .description(description)
                .requestSchema(requestSchema)
                .responseSchema(responseSchema)
                .requestFields(requestFieldsSnippet)
                .requestParameters(requestParameters)
                .pathParameters(pathParameters)
                .responseFields(responseFieldsSnippet)
                .build();
    }

    @NotNull
    protected List<FieldDescriptor> getErrorResponseAuthFields(String errorKey, String errorDescription) {
        return Arrays.asList(
                fieldWithPath("errorCode").description(DATA_INVALID_ERROR.getErrorCode()),
                fieldWithPath("errorMessage").description(DATA_INVALID_ERROR.getErrorMessage()),
                fieldWithPath(errorKey).description(errorDescription)
        );
    }
}
