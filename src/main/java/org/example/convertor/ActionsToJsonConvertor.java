package org.example.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Action;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@WritingConverter
public class ActionsToJsonConvertor implements Converter<Map<LocalDateTime, Action>, String> {

    private final ObjectMapper objectMapper;
    public ActionsToJsonConvertor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(Map<LocalDateTime,Action> actions) {
//            String result = "{";
//            for (Map.Entry<Date, Action> entry : actions.entrySet()) {
//                result += entry.getKey().toString() + ":" + actionToJsonConvertor.convert(entry.getValue()) + ",";
//            }
//            result+="}";
//            return result;
        try {
            return objectMapper.writeValueAsString(actions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
