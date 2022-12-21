package com.finalproject.hwangjunha_team3.domain.dto;

import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;
}
