package com.example.document;


import com.epages.restdocs.apispec.ParameterDescriptorWithType;
import lombok.*;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestDocumentation {
    private String identifier;
    private String tag;
    private String summary;
    private String description;
    private String requestSchema;
    private String responseSchema;
    private List<FieldDescriptor> requestFields;
    private List<FieldDescriptor> responseFields;
    private List<ParameterDescriptorWithType> pathParameters;
    private List<ParameterDescriptorWithType> requestParameters;
    private List<RequestPartDescriptor> requestParts;
}
