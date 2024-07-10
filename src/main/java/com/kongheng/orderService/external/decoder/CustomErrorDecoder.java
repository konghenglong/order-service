package com.kongheng.orderService.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kongheng.orderService.exception.CustomException;
import com.kongheng.orderService.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper mapper = new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        try {
            ErrorResponse errorResponse = mapper
                .readValue(response.body().asInputStream(), ErrorResponse.class);

            return new CustomException(
                errorResponse.getErrorMessage(),
                errorResponse.getErrorCode(),
                response.status()
            );

        } catch (IOException e) {
            throw new CustomException(
                "Internal Server Error",
                "INTERNAL_SERVER_ERROR",
                500);
        }
    }
}
