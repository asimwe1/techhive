package com.techhive.tech.Models;


import jakarta.persistence.*;

@Entity
@Table(name = "job_category")
public class JobCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String category;

    public JobCategory(Integer categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }

    public JobCategory(String category) {
        this.category = category;
    }

    public JobCategory() {
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
