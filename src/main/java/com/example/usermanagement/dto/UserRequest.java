package com.example.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "username 不能为空")
    private String username;

    @Email(message = "email 格式不正确")
    @NotBlank(message = "email 不能为空")
    private String email;

    @Min(value = 0, message = "age 不能小于 0")
    @Max(value = 150, message = "age 不能大于 150")
    private Integer age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
