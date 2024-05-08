package com.ecocursos.ecocursos.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiError {

    private int status;
    private String message;
    private String path;
    private long timestamp;

}
