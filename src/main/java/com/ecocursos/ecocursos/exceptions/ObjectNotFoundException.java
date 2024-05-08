package com.ecocursos.ecocursos.exceptions;

import lombok.Data;

@Data
public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
