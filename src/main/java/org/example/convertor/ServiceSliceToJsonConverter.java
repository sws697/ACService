package org.example.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ServiceSlice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class ServiceSliceToJsonConverter implements Converter<ServiceSlice, String> {

    private final ObjectMapper objectMapper;

    public ServiceSliceToJsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(ServiceSlice serviceSlice) {
        try {
            return objectMapper.writeValueAsString(serviceSlice);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}