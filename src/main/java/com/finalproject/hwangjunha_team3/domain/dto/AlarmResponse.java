package com.finalproject.hwangjunha_team3.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.hwangjunha_team3.domain.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AlarmResponse {
    private Integer id;
    private String alarmType;
    private Integer fromUserId;
    private Integer targetId;
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static Page<AlarmResponse> toDtoList(Page<Alarm> alarms) {
        return alarms.map(m -> AlarmResponse.builder()
                .id(m.getId())
                .alarmType(m.getAlarmType())
                .fromUserId(m.getFromUserId())
                .targetId(m.getTargetId())
                .text(m.getText())
                .createdAt(m.getCreatedAt())
                .build());
    }
}
