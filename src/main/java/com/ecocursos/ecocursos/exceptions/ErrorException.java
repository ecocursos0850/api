package com.ecocursos.ecocursos.exceptions;

import lombok.Data;

@Data
public class ErrorException extends RuntimeException{
    public ErrorException(String message) {
        super(message);
    }
}
