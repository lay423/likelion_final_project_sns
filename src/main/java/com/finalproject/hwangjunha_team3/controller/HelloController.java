package com.finalproject.hwangjunha_team3.controller;

import com.finalproject.hwangjunha_team3.service.AlgorithmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class HelloController {

    private final AlgorithmService algorithmService;


    @GetMapping("/hello")
    public String hello(){
        return "황준하";
    }

    @GetMapping("/hello/{num}")
    public String sumOfDigit(@PathVariable Integer num) {
        return algorithmService.sumOfDigit(num);
    }

}
