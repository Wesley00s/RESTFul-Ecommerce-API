package org.wesley.ecommerce.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ExceptionDetails {
    private String title;
    private LocalDateTime timestamp;
    private Integer status;
    private String exception;
    private Map<String, String> details = new HashMap<>();

}