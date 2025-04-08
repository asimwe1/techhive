package com.techhive.Technology.DTO;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDTO {
    private Integer id;
    @NotBlank(message = "Job name is required")
    private String name;
    private String description;
    private Integer clientId;
    private String clientUsername;
    @NotNull(message = "Category ID is required")
    private Integer categoryId;
    private String categoryName;
    @NotNull(message = "Minimum salary is required")
    @Positive(message = "Minimum salary must be positive")
    private Double salaryRangeLower;
    @NotNull(message = "Maximum salary is required")
    @Positive(message = "Maximum salary must be positive")
    private Double salaryRangeUpper;
    private String status;
    @NotEmpty(message = "At least one skill is required")
    private List<String> skills;
    private LocalDateTime createdAt;
}