package com.techhive.Technology.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "salary_range_lower")
    private Double salaryRangeLower;

    @Column(name = "salary_range_upper")
    private Double salaryRangeUpper;

    public Job(Integer id, String name, Integer categoryId, Integer clientId, Double salaryRangeLower, Double salaryRangeUpper) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.clientId = clientId;
        this.salaryRangeLower = salaryRangeLower;
        this.salaryRangeUpper = salaryRangeUpper;
    }

    public Job(String name, Integer categoryId, Integer clientId, Double salaryRangeLower, Double salaryRangeUpper) {
        this.name = name;
        this.categoryId = categoryId;
        this.clientId = clientId;
        this.salaryRangeLower = salaryRangeLower;
        this.salaryRangeUpper = salaryRangeUpper;
    }

    public Job() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer Client) {
        this.clientId = Client;
    }

    public Double getSalaryRangeLower() {
        return salaryRangeLower;
    }

    public void setSalaryRangeLower(Double salaryRangeLower) {
        this.salaryRangeLower = salaryRangeLower;
    }

    public Double getSalaryRangeUpper() {
        return salaryRangeUpper;
    }

    public void setSalaryRangeUpper(Double salaryRangeUpper) {
        this.salaryRangeUpper = salaryRangeUpper;
    }

}
