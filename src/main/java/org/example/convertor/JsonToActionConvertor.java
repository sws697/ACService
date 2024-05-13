package org.example.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Action;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class JsonToActionConvertor implements Converter<String, Action> {

    private final ObjectMapper objectMapper;

    public JsonToActionConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Action convert(String action) {
        try {
            return objectMapper.readValue(action, Action.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
