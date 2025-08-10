package com.jc.template.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InternalServerException extends RuntimeException {
    private String code;
    private String errorMessage;
    private Throwable throwable;
}
