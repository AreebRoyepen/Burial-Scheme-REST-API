package com.example.BurialSchemeRestApi.api;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorMessage extends Message {
    public final String error;

    @Builder(builderMethodName = "errorMessageBuilder")
    public ErrorMessage(String error, String message) {
        super(message);
        this.error = error;
    }
}
