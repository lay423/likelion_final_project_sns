package com.finalproject.hwangjunha_team3.controller;

import com.finalproject.hwangjunha_team3.domain.Response;
import com.finalproject.hwangjunha_team3.domain.dto.AlarmResponse;
import com.finalproject.hwangjunha_team3.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AlarmController {

    private final PostService postService;

    @GetMapping("/alarms")
    public Response<Page<AlarmResponse>> getAlarms(@PageableDefault(size = 20)
                                             @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                  Authentication authentication) {
        return Response.success(postService.getAlarm(pageable, authentication.getName()));

    }
}
