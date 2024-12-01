package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.model.Evaluator;
import com.example.SmartCommunity.repository.EvaluatorRepository;
import com.example.SmartCommunity.service.EvaluatorService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EvaluatorServiceImpl implements EvaluatorService {

    private final EvaluatorRepository evaluatorRepository;

    public EvaluatorServiceImpl(EvaluatorRepository evaluatorRepository) {
        this.evaluatorRepository = evaluatorRepository;
    }

    @Override
    public List<Evaluator> getAllEvaluators() {
        return evaluatorRepository.findAll();
    }

    @Override
    public List<Evaluator> searchEvaluators(String name, String department) {
        // 如果人名和部门都为空，返回所有评价人员
        if (name == null && department == null) {
            return getAllEvaluators();
        }

        // 调用仓库方法，根据传入的条件进行筛选
        return evaluatorRepository.findByNameContainingAndDepartmentContaining(name != null ? name : "",
                department != null ? department : "");
    }
}
