package com.example.todo.service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public String test() {
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        todoRepository.save(entity);
        TodoEntity savedEntity = todoRepository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity) {
        // Validations
        validate(entity);

        // save
        todoRepository.save(entity);

        log.info("Entity ID : {} is saved", entity.getId());

        // return
        return todoRepository.findByUserId(entity.getUserId());
    }

    private void validate(final TodoEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null!");
            throw new RuntimeException("Entity cannot be null!");
        }

        if (entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw  new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> findAll(final String userId) {
        return todoRepository.findByUserId(userId);
    }
}
