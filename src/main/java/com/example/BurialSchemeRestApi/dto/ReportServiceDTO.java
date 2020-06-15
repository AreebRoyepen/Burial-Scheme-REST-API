package com.example.BurialSchemeRestApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class ReportServiceDTO {

    HttpHeaders httpHeaders;
    MediaType contentType;
    ByteArrayInputStream inputStream;

}
