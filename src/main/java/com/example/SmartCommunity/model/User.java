package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "SmartCommunity", uniqueConstraints = {
        @UniqueConstraint(name = "user_name_idx", columnNames = {"user_name"}),
        @UniqueConstraint(name = "phone_number_idx", columnNames = {"phone_number"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "user_type", nullable = false)
    private Byte userType;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        if (this.avatar == null || this.avatar.isEmpty()) {
            this.avatar = "https://1st-bucket.oss-cn-shanghai.aliyuncs.com/Avatar/defaultAvatar.jpg";
        }
        this.userType = Byte.valueOf("1");
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}