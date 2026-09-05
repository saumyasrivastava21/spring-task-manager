package com.example.spring_task_manager.config;

import com.example.spring_task_manager.entity.Priority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PriorityParamConverter implements Converter<String, Priority> {
    @Override
    public Priority convert(String source) {
        return Priority.valueOf(source.toUpperCase());
    }
}
