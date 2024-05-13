package org.example.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ServiceSlice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class JsonToServiceSliceConverter implements Converter<String, ServiceSlice> {

    private final ObjectMapper objectMapper;

    public JsonToServiceSliceConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ServiceSlice convert(String source) {
        try {
            return objectMapper.readValue(source, ServiceSlice.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}