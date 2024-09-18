package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User add(User user) {
        String addingUsersEmail = user.getEmail();

        if (addingUsersEmail == null || addingUsersEmail.isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        Set<String> existEmails = getExistEmails();

        if (existEmails.contains(addingUsersEmail)) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);

        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        Set<String> existEmails = getExistEmails();

        if (user.getEmail() != null && existEmails.contains(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());

            if (user.getEmail() != null) {
                oldUser.setEmail(user.getEmail());
            }
            if (user.getUsername() != null) {
                oldUser.setUsername(user.getUsername());
            }
            if (user.getPassword() != null) {
                oldUser.setPassword(user.getPassword());
            }

            oldUser.setRegistrationDate(Instant.now());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
    }

    public User getById(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        } else {
            return user.get();
        }
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }


    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private Set<String> getExistEmails() {
        return users.values().stream().map(User::getEmail)
                .collect(Collectors.toSet());
    }

}
