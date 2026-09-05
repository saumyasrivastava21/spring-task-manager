package com.example.spring_task_manager.dto;

import java.util.List;

public record ResponsePage<T>(List<T> data, Long nextCursor, boolean hasNext, long size) {
}
