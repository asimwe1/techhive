package com.techhive.Technology.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;


    @Column(name = "category_id")
    private Integer categoryId;

    @Type(RangeUserType.class)
    @Column(name = "salary_range", columnDefinition = "numrange")
    private Range<Double> salaryRange;


    @Column(name = "client_id")
    private Integer clientId;

    public Job(Integer id, String name, Integer categoryId, Range<Double> salaryRange, Integer clientId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.salaryRange = salaryRange;
        this.clientId = clientId;
    }

    public Job(String name, Integer categoryId, Range<Double> salaryRange, Integer clientId) {
        this.name = name;
        this.categoryId = categoryId;
        this.salaryRange = salaryRange;
        this.clientId = clientId;
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

    public Range getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(Range salaryRange) {
        this.salaryRange = salaryRange;
    }

    public Integer getClientId() {
        return clientId;
    }
    public void setClientId(Integer Client) {
        this.clientId = Client;
    }
}
