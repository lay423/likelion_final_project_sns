package com.finalproject.hwangjunha_team3.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;


class AlgorithmServiceTest {

    AlgorithmService algorithmService = new AlgorithmService();

    @Test
    @DisplayName("자릿수 합 잘 구하는지")
    void sumOfDigit() {
        Assertions.assertEquals(String.valueOf(21), algorithmService.sumOfDigit(687));
//        assertEquals(21, algorithmService.sumOfDigit(687)
//        assertEquals(22, algorithmService.sumOfDigit(787));
//        assertEquals(0, algorithmService.sumOfDigit(0));
//        assertEquals(5, algorithmService.sumOfDigit(11111));

    }
}