package org.example.convertor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Action;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@ReadingConverter
public class JsonToActionsConvertor implements Converter< String,Map<LocalDateTime, Action>> {

    private final ObjectMapper objectMapper;

    public JsonToActionsConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<LocalDateTime,Action> convert(String actions) {
        try {
            return objectMapper.readValue(actions, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}