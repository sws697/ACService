package org.example.convertor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Action;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;



@Component
@WritingConverter
public class ActionToJsonConvertor implements Converter<Action, String> {

    private final ObjectMapper objectMapper;

    public ActionToJsonConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(Action action) {
        try {
            return objectMapper.writeValueAsString(action);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}