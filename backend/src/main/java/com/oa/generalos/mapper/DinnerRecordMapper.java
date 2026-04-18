package com.oa.generalos.mapper;

import com.oa.generalos.entity.DinnerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DinnerRecordMapper {
    List<DinnerRecord> findAll();

    DinnerRecord findById(@Param("id") Long id);

    List<DinnerRecord> findByDateRange(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    List<DinnerRecord> findByDepartment(@Param("department") String department);

    int insert(DinnerRecord record);

    int update(DinnerRecord record);

    int deleteById(@Param("id") Long id);

    BigDecimal sumAmountByDateRange(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    BigDecimal sumAmountByDepartment(@Param("department") String department);

    int count();

    int countByDateRange(@Param("startDate") LocalDate startDate,
                         @Param("endDate") LocalDate endDate);
}
