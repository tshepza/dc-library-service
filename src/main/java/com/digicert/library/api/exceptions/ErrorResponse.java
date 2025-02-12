package com.digicert.library.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private String type;
    private String metaData;
    private List<String> error;
}
