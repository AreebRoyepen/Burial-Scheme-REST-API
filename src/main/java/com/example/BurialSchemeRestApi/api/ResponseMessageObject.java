package com.example.BurialSchemeRestApi.api;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ResponseMessageObject extends Message {
    public final Object data;

    public ResponseMessageObject(Object data, String message, String info) {
        super(message, info);
        this.data = data;
        this.info = info;
    }

    public ResponseMessageObject(Object data, String message) {
        super(message);
        this.data = data;
    }
}
