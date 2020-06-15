package com.example.BurialSchemeRestApi.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationException extends Exception {
    @Getter
    private String message;
}
