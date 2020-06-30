package com.example.BurialSchemeRestApi.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtRefreshRequest implements Serializable {

    private static final long serialVersionUID = -8091879091924048844L;

    @NonNull String refreshToken;
    @NonNull String username;

}
