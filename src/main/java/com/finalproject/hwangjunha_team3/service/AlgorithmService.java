package com.finalproject.hwangjunha_team3.service;

import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {

    public String sumOfDigit(Integer num) {
        int sum=0;
        while (num != 0) {
            sum += num%10;
            num /=10;
        }
        return String.valueOf(sum);
    }

}
