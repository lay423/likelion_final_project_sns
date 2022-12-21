package com.finalproject.hwangjunha_team3.exceptionManager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalReviewAppException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    @Override
    public String toString() {
        if(message==null)return errorCode.getMessage();
        return String.format("%s %s", errorCode.getMessage(), message);
    }
}
