package com.oa.generalos.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DictCategoryVO {
    private Long id;
    private String code;
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DictItemVO> items;
}
