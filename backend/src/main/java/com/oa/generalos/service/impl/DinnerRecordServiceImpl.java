package com.oa.generalos.service.impl;

import com.oa.generalos.entity.DinnerRecord;
import com.oa.generalos.entity.User;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.DinnerRecordMapper;
import com.oa.generalos.mapper.UserMapper;
import com.oa.generalos.service.DinnerRecordService;
import com.oa.generalos.vo.DinnerRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DinnerRecordServiceImpl implements DinnerRecordService {

    @Autowired
    private DinnerRecordMapper dinnerRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<DinnerRecordVO> getAllRecords() {
        List<DinnerRecord> records = dinnerRecordMapper.findAll();
        return records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DinnerRecordVO> getRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<DinnerRecord> records = dinnerRecordMapper.findByDateRange(startDate, endDate);
        return records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DinnerRecordVO> getRecordsByDepartment(String department) {
        List<DinnerRecord> records = dinnerRecordMapper.findByDepartment(department);
        return records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public DinnerRecordVO getRecordById(Long id) {
        DinnerRecord record = dinnerRecordMapper.findById(id);
        if (record == null) {
            throw new BusinessException(404, "会餐记录不存在");
        }
        return convertToVO(record);
    }

    @Override
    public void createRecord(LocalDate recordDate, String department, Integer participantCount,
                            BigDecimal amount, String purpose, String responsiblePerson,
                            String invoicePath, Long createdBy) {
        DinnerRecord record = new DinnerRecord();
        record.setRecordDate(recordDate);
        record.setDepartment(department);
        record.setParticipantCount(participantCount);
        record.setAmount(amount);
        record.setPurpose(purpose);
        record.setResponsiblePerson(responsiblePerson);
        record.setInvoicePath(invoicePath);
        record.setCreatedBy(createdBy);

        dinnerRecordMapper.insert(record);
    }

    @Override
    public void updateRecord(Long id, LocalDate recordDate, String department,
                            Integer participantCount, BigDecimal amount, String purpose,
                            String responsiblePerson, String invoicePath) {
        DinnerRecord existing = dinnerRecordMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "会餐记录不存在");
        }

        existing.setRecordDate(recordDate);
        existing.setDepartment(department);
        existing.setParticipantCount(participantCount);
        existing.setAmount(amount);
        existing.setPurpose(purpose);
        existing.setResponsiblePerson(responsiblePerson);
        existing.setInvoicePath(invoicePath);

        dinnerRecordMapper.update(existing);
    }

    @Override
    public void deleteRecord(Long id) {
        DinnerRecord existing = dinnerRecordMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "会餐记录不存在");
        }
        dinnerRecordMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getStatistics(LocalDate startDate, LocalDate endDate) {
        int count = dinnerRecordMapper.countByDateRange(startDate, endDate);
        BigDecimal totalAmount = dinnerRecordMapper.sumAmountByDateRange(startDate, endDate);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("startDate", startDate);
        statistics.put("endDate", endDate);
        statistics.put("count", count);
        statistics.put("totalAmount", totalAmount);
        return statistics;
    }

    @Override
    public BigDecimal getTotalAmount(LocalDate startDate, LocalDate endDate) {
        return dinnerRecordMapper.sumAmountByDateRange(startDate, endDate);
    }

    private DinnerRecordVO convertToVO(DinnerRecord record) {
        DinnerRecordVO vo = new DinnerRecordVO();
        BeanUtils.copyProperties(record, vo);

        if (record.getCreatedBy() != null) {
            User user = userMapper.findById(record.getCreatedBy());
            if (user != null) {
                vo.setCreatedByName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
        }

        return vo;
    }
}
