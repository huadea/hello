package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserRequest;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void shouldCreateUserWithIncrementId() {
        UserRequest req1 = buildRequest("alice", "alice@example.com", 20);
        UserRequest req2 = buildRequest("bob", "bob@example.com", 22);

        User user1 = userService.createUser(req1);
        User user2 = userService.createUser(req2);

        assertEquals(1L, user1.getId());
        assertEquals(2L, user2.getId());
        assertEquals("alice", user1.getUsername());
    }

    @Test
    void shouldReturnUsersSortedById() {
        userService.createUser(buildRequest("u1", "u1@example.com", 18));
        userService.createUser(buildRequest("u2", "u2@example.com", 19));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.get(0).getId() < users.get(1).getId());
    }

    @Test
    void shouldUpdateExistingUser() {
        User created = userService.createUser(buildRequest("old", "old@example.com", 30));

        User updated = userService.updateUser(created.getId(), buildRequest("new", "new@example.com", 31));

        assertEquals(created.getId(), updated.getId());
        assertEquals("new", updated.getUsername());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(31, updated.getAge());
    }

    @Test
    void shouldDeleteExistingUser() {
        User created = userService.createUser(buildRequest("toDel", "del@example.com", 30));

        userService.deleteUser(created.getId());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(created.getId()));
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(999L, buildRequest("x", "x@example.com", 1)));
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(999L));
    }

    private UserRequest buildRequest(String username, String email, Integer age) {
        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setAge(age);
        return request;
    }
}
