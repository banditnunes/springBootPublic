package br.com.alura.curso.springboot.forum.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

public class CustomResponseEntity<T> extends ResponseEntity implements Serializable {
    public CustomResponseEntity(HttpStatus status) {
        super(status);
    }

    public CustomResponseEntity(Object body, HttpStatus status) {
        super(body, status);
    }

    public CustomResponseEntity(MultiValueMap headers, HttpStatus status) {
        super(headers, status);
    }

    public CustomResponseEntity(Object body, MultiValueMap headers, HttpStatus status) {
        super(body, headers, status);
    }


}
