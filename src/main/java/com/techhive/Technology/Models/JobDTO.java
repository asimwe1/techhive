package com.techhive.Technology.Models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer clientId;
    private String clientUsername;
    private Integer categoryId;
    private String categoryName;
    private Double salaryRangeLower;
    private Double salaryRangeUpper;
    private String status;
    private List<String> skills;
    private LocalDateTime createdAt;
}