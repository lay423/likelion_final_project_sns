package com.finalproject.hwangjunha_team3.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@SQLDelete(sql = "UPDATE \"Alarm\" SET deleted_at = current_timestamp WHERE id = ?")
public class Alarm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String alarmType;
    private LocalDateTime deletedAt;
    private Integer fromUserId;
    private Integer targetId;
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
