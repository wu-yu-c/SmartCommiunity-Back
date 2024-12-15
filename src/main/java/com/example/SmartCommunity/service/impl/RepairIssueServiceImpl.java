package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.RepairIssueResponse;
import com.example.SmartCommunity.model.ImageFile;
import com.example.SmartCommunity.model.VideoFile;
import com.example.SmartCommunity.model.Repairissue;
import com.example.SmartCommunity.repository.ImageFileRepository;
import com.example.SmartCommunity.repository.VideoFileRepository;
import com.example.SmartCommunity.repository.RepairIssueRepository;
import com.example.SmartCommunity.service.RepairIssueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class RepairIssueServiceImpl implements RepairIssueService {

    @Autowired
    private RepairIssueRepository repairIssueRepository;

    @Autowired
    private ImageFileRepository imageFileRepository;

    @Autowired
    private VideoFileRepository videoFileRepository;

    // 实现原有的CRUD方法
    @Override
    public List<Repairissue> findAll() {
        return repairIssueRepository.findAll();
    }

    @Override
    public Repairissue findById(Integer id) {
        return repairIssueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repair issue not found"));
    }

    @Override
    public Repairissue save(Repairissue repairIssue) {
        return repairIssueRepository.save(repairIssue);
    }

    @Override
    public Repairissue update(Repairissue repairIssue) {
        if (!repairIssueRepository.existsById(repairIssue.getId())) {
            throw new RuntimeException("Repair issue not found");
        }
        return repairIssueRepository.save(repairIssue);
    }

    @Override
    public void deleteById(Integer id) {
        repairIssueRepository.deleteById(id);
    }

    // 实现新增的文件处理方法
    @Override
    public Repairissue createRepairIssueWithFiles(RepairIssueDTO dto, MultipartFile imageFile, MultipartFile videoFile) {
        Repairissue repairIssue = new Repairissue();
        BeanUtils.copyProperties(dto, repairIssue);

        // 处理图片
        if (imageFile != null && !imageFile.isEmpty()) {
            ImageFile image = new ImageFile();
            try {
                image.setFileData(imageFile.getBytes());
                image = imageFileRepository.save(image);
                repairIssue.setImageId(image.getId().intValue());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image file", e);
            }
        }

        // 处理视频
        if (videoFile != null && !videoFile.isEmpty()) {
            VideoFile video = new VideoFile();
            try {
                video.setFileData(videoFile.getBytes());
                video = videoFileRepository.save(video);
                repairIssue.setVideoId(video.getId().intValue());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process video file", e);
            }
        }

        return repairIssueRepository.save(repairIssue);
    }

    @Override
    public RepairIssueResponse getRepairIssueWithFiles(Integer id) {
        Repairissue repairIssue = findById(id);

        RepairIssueResponse response = new RepairIssueResponse();
        BeanUtils.copyProperties(repairIssue, response);

        // 获取图片
        if (repairIssue.getImageId() != null) {
            ImageFile image = imageFileRepository.findById(repairIssue.getImageId().longValue())
                    .orElse(null);
            if (image != null) {
                response.setImageData(image.getFileData());
            }
        }

        // 获取视频
        if (repairIssue.getVideoId() != null) {
            VideoFile video = videoFileRepository.findById(repairIssue.getVideoId().longValue())
                    .orElse(null);
            if (video != null) {
                response.setVideoData(video.getFileData());
            }
        }

        return response;
    }
}