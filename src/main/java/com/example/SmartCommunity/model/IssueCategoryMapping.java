package com.example.SmartCommunity.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class IssueCategoryMapping {
    @EmbeddedId
    private IssueCategoryMappingId id;

}