package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.Evaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluatorRepository extends JpaRepository<Evaluator, Long> {
    // 根据人名（模糊查询）和所属部门（精确查询）筛选评价人员
    List<Evaluator> findByNameContainingAndDepartmentContaining(String name, String department);
}
