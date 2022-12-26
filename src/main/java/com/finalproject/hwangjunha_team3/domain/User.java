package com.finalproject.hwangjunha_team3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id = null;

    @Column(unique = true)
    private String userName;
    private String password;
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;


    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
