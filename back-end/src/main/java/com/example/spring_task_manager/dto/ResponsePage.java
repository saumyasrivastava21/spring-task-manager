package com.example.spring_task_manager.dto;

import java.util.List;

public class ResponsePage<T> {

    private List<T> data;
    private Long nextCursor;
    private boolean hasNext;
    private long size;

    public ResponsePage(List<T> data, Long nextCursor, boolean hasNext, long size) {
        this.data = data;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
        this.size = size;
    }

    public List<T> getData() {
        return data.stream().toList();
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Long nextCursor) {
        this.nextCursor = nextCursor;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
