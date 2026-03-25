package com.example.usermanagement.controller;

import com.example.usermanagement.exception.GlobalExceptionHandler;
import com.example.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserController controller = new UserController(new UserService());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateAndQueryUser() throws Exception {
        String body = """
                {
                  \"username\": \"tom\",
                  \"email\": \"tom@example.com\",
                  \"age\": 28
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("tom"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("tom@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFailed() throws Exception {
        String body = """
                {
                  \"username\": \"\",
                  \"email\": \"bad-mail\",
                  \"age\": -1
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("请求参数校验失败"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        String body = """
                {
                  \"username\": \"to-delete\",
                  \"email\": \"delete@example.com\",
                  \"age\": 35
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("用户不存在，id=1"));
    }
}
