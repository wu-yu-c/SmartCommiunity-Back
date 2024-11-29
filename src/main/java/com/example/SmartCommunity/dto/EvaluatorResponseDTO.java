package com.example.SmartCommunity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EvaluatorResponseDTO {
    @JsonProperty("id")
    private Long ID;

    @JsonProperty("name")
    private String name;

    @JsonProperty("position")
    private String position;

    @JsonProperty("avatar")
    private String avatar;
}
