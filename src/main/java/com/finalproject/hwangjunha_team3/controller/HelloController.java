package com.finalproject.hwangjunha_team3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "황준하";
    }

    @GetMapping("/hello/{num}")
    public String sumOfDigit(@PathVariable Integer num) {
        int sum=0;
        while (num != 0) {
            sum += num%10;
            num /=10;
        }
        log.info("sum:{}", sum);
        return String.valueOf(sum);
    }
}
