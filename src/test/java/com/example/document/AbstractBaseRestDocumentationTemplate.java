package com.example.document;

import com.example.villagerservice.common.exception.ErrorResponse;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBaseRestDocumentationTemplate extends BaseRestDocumentationTemplate {
    private final String tag;
    private final List<FieldDescriptor> requestFieldDescriptor;
    private final String requestSimpleName;

    public AbstractBaseRestDocumentationTemplate(String tag, List<FieldDescriptor> requestFieldDescriptor,
                                                 String requestSimpleName) {
        this.tag = tag;
        this.requestFieldDescriptor = requestFieldDescriptor;
        this.requestSimpleName = requestSimpleName;
    }

    public RestDocumentation createRestDocumentation(String summary,
                                                     String identifier,
                                                     String errorKey,
                                                     String errorDescription) {
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

    public RestDocumentation createRestDocumentation(String summary, String identifier) {
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
}
