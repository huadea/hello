package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserRequest;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public User createUser(UserRequest request) {
        long id = idGenerator.getAndIncrement();
        User user = new User(id, request.getUsername(), request.getEmail(), request.getAge());
        users.put(id, user);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>(users.values());
        result.sort(Comparator.comparing(User::getId));
        return result;
    }

    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    public User updateUser(Long id, UserRequest request) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        }
        User updated = new User(id, request.getUsername(), request.getEmail(), request.getAge());
        users.put(id, updated);
        return updated;
    }

    public void deleteUser(Long id) {
        User removed = users.remove(id);
        if (removed == null) {
            throw new UserNotFoundException(id);
        }
    }
}
