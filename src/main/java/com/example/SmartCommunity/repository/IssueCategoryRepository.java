package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.IssueCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueCategoryRepository extends JpaRepository<IssueCategory, Long> {
}
