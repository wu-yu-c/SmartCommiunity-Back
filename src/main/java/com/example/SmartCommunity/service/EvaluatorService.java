package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.Evaluator;
import java.util.List;

public interface EvaluatorService {
    List<Evaluator> getAllEvaluators();

    // 根据人名和部门筛选评价人员
    List<Evaluator> searchEvaluators(String name, String department);
}
