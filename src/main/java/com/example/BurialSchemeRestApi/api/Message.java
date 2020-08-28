package com.example.BurialSchemeRestApi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract public class Message implements Serializable {
    public String message;
    public String info;

    public Message(String message) {
        this.message = message;
    }
}
