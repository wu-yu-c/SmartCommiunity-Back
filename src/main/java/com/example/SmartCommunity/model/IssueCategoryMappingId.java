package com.example.SmartCommunity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class IssueCategoryMappingId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 8751662689604559135L;
    @NotNull
    @Column(name = "IssueID", nullable = false)
    private Integer issueID;

    @NotNull
    @Column(name = "CategoryID", nullable = false)
    private Integer categoryID;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        IssueCategoryMappingId entity = (IssueCategoryMappingId) o;
        return Objects.equals(this.issueID, entity.issueID) &&
                Objects.equals(this.categoryID, entity.categoryID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueID, categoryID);
    }

}