package com.example.SmartCommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatedStaffInfoDTO
{
    private Long staffId;
    private String staffName;
    private String phoneNumber;
    private String avatar;
}
