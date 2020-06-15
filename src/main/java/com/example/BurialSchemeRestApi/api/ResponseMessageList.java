package com.example.BurialSchemeRestApi.api;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ResponseMessageList extends Message {
    public final List<?> data;

    public ResponseMessageList(List<?> data, String message, String info) {
        super(message, info);
        this.data = data;
        this.info = info;
    }

    public ResponseMessageList(List<?> data, String message) {
        super(message);
        this.data = data;
    }
}
