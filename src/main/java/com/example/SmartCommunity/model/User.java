package com.example.SmartCommunity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp; // 导入 Timestamp
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long userID;

    @Column(name = "UserName", nullable = false, unique = true)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "UserPhone", unique = true)
    private String userPhone;

    @Column(name = "CreatedTime", insertable = false, updatable = false)
    private Timestamp createdTime;

    @Column(name = "Avatar")
    @ColumnDefault("https://first-tekcub.oss-cn-shanghai.aliyuncs.com/Avatar/e6fc3672-f78c-4328-b93d-124f38a3aa35.jpg")
    private String avatar;
    // Getters and Setters
    // Lombok 的 @Getter 和 @Setter 自动生成
}