package com.example.SmartCommunity.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SmartCommunity.dto.ServiceDTO;
import com.example.SmartCommunity.dto.StaffWithEvaluatedCount;
import com.example.SmartCommunity.dto.StaffWithServicesDTO;
import com.example.SmartCommunity.dto.UpdatedStaffInfoDTO;
import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.repository.EventEvaluationRepository;
import com.example.SmartCommunity.repository.StaffRepository;
import com.example.SmartCommunity.service.StaffService;
import com.example.SmartCommunity.util.OSSUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final EventEvaluationRepository eventEvaluationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public StaffServiceImpl(StaffRepository staffRepository, EventEvaluationRepository eventEvaluationRepository) {
        this.staffRepository = staffRepository;
        this.eventEvaluationRepository = eventEvaluationRepository;
    }

    @Override
    public void changePassword(String name, String phone, String newPassword){
        // 查找用户
        Staff staff = (Staff) staffRepository.findbyNameAndPhoneNumber(name, phone)
                .orElseThrow(() -> new RuntimeException("Staff not found with the provided name and phone"));
        // 更新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        staff.setPassword(encodedPassword);
        staffRepository.save(staff);
    }

    @Override
    public Map<String, Object> login(Long staffId, String phone, String password){
        Map<String, Object> response = new HashMap<>();
        // 输入合法性验证
        if ((staffId == null || staffId == 0L) && StringUtils.isBlank(phone)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "职工工号或电话号码不能为空");
            return response;
        }

        if (StringUtils.isBlank(password)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "密码不能为空");
            return response;
        }
        try {
            Staff staff = null;
            // 根据输入查询职工
            if (staffId != null && staffId > 0) {
                staff = staffRepository.findStaffByStaffID(staffId);
            }
            if (staff == null && StringUtils.isNotBlank(phone)) {
                staff = staffRepository.findStaffByPhoneNumber(phone);
            }

            // 如果职工未找到
            if (staff == null) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "职工不存在");
                return response;
            }

            // 验证密码
            if (!passwordEncoder.matches(password, staff.getPassword())) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "密码错误");
                return response;
            }

            // 登录成功，使用 Sa-Token 生成 Token
            StpUtil.login(staff.getStaffID());

            response.put("code", HttpStatus.OK.value());
            response.put("message", "登录成功");
            response.put("userID",staff.getStaffID());
            response.put("token", StpUtil.getTokenValue());
            return response;

        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public List<Staff> searchStaff(String name, String department) {
        // 如果人名和部门都为空，返回所有评价人员
        if (name == null && department == null) {
            return getAllStaff();
        }

        // 调用仓库方法，根据传入的条件进行筛选
        return staffRepository.findByNameContainingAndDepartmentContaining(name != null ? name : "",
                department != null ? department : "");
    }

    public StaffWithServicesDTO getStaffWithServices(Long staffId) {
        // 获取工作人员信息
        Staff staff = Optional.ofNullable(staffRepository.findStaffByStaffID(staffId))
                .orElseThrow(() -> new NoSuchElementException("Staff not found with ID: " + staffId));

        // 获取工作人员的服务信息
        List<ServiceDTO> services =
                eventEvaluationRepository.findServicesByStaffId(staffId);

        // 封装为 DTO
        return new StaffWithServicesDTO(
                staff.getStaffID(),
                staff.getName(),
                staff.getAvatar(),
                staff.getPosition(),
                staff.getDepartment(),
                staff.getAverageRating(),
                services
        );
    }

    public List<StaffWithEvaluatedCount> getStaffEvaluationCounts() {
        try {
            List<StaffWithEvaluatedCount> staffEvaluationCounts = eventEvaluationRepository.findStaffEvaluationCounts();
            if (staffEvaluationCounts == null || staffEvaluationCounts.isEmpty()) {
                throw new Exception("没有找到工作人员的评价记录.");
            }
            return staffEvaluationCounts;
        } catch (Exception e) {
            // 记录日志，异常信息通过标准异常类抛出
            e.printStackTrace();  // 可以根据实际情况使用日志记录工具
            throw new RuntimeException("查询工作人员评价次数失败，请稍后重试.");
        }
    }

    @Override
    public Map<String,Object> getStaffById(Long staffId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Staff staff = staffRepository.findStaffByStaffID(staffId);
            if (staff != null) {
                response.put("code", HttpStatus.OK.value());
                response.put("staff", staff);
                return response;
            }else{
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "不存在该员工");
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UpdatedStaffInfoDTO updateProfile(Long staffId, String name, String phoneNumber, MultipartFile avatar) {
        // 查找职工信息
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));
        Optional<Staff> repeatStaff = Optional.ofNullable(staffRepository.findStaffByPhoneNumber(phoneNumber));
        if (repeatStaff.isPresent() && !repeatStaff.get().getStaffID().equals(staffId)) {
            throw new IllegalArgumentException("Phone number already in use by another staff member.");
        }
        // 更新姓名和电话号码
        if(name!=null)
            staff.setName(name);
        if(phoneNumber!=null)
            staff.setPhoneNumber(phoneNumber);

        // 如果有上传头像，处理上传并更新头像路径
        if (avatar != null && !avatar.isEmpty()) {
            String avatarUrl = staffAvatar(staffId, avatar);
            if (!avatarUrl.equals("failure"))
                staff.setAvatar(avatarUrl);
            else
                throw new RuntimeException("头像上传失败。");
        }

        // 保存更新后的职工信息
        staffRepository.save(staff);
        return new UpdatedStaffInfoDTO(staff.getStaffID(),staff.getName(),staff.getPhoneNumber(),staff.getAvatar());
    }

    @Override
    public String staffAvatar(Long id, MultipartFile file) {
        try {
            // 1. 获取用户信息
            Staff user = staffRepository.findById(id).orElseThrow(() -> new RuntimeException("职工不存在"));
            String defaultAvatar = "https://first-tekcub.oss-cn-shanghai.aliyuncs.com/Avatar/" +
                    "e6fc3672-f78c-4328-b93d-124f38a3aa35.jpg";

            // 2. 处理旧头像
            String oldAvatarUrl = user.getAvatar();
            if (oldAvatarUrl != null&& !oldAvatarUrl.equals(defaultAvatar)) {
                // 删除旧头像
                String oldAvatarKey = oldAvatarUrl.replace("https://first-tekcub.oss-cn-shanghai.aliyuncs.com/", "");
                OSSUtils.deleteFile(oldAvatarKey);
            }

            // 3. 上传新头像
            // 使用 UUID 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID() + fileExtension;
            String objectName = "Avatar/" + uniqueFileName;
            String newAvatarUrl = OSSUtils.uploadFileToOSS(file, objectName);

            if (newAvatarUrl.equals("failure")) {
                return "failure";
            }

            // 4. 更新用户信息
            String fileUrl = "https://first-tekcub.oss-cn-shanghai.aliyuncs.com/" + objectName;
            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
