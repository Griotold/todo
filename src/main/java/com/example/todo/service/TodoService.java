package com.example.todo.service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.EntityReference;
import java.util.List;
import java.util.Optional;

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

    /**
     * 생성
     * */
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

    /**
     * 조회
     * */
    public List<TodoEntity> findAll(final String userId) {
        return todoRepository.findByUserId(userId);
    }

    /**
     * 수정
     */
    public List<TodoEntity> update(final TodoEntity entity) {
        validate(entity);

        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            todoRepository.save(todo);
        });

        return findAll(entity.getUserId());

    }

    /**
     * 삭제
     */
    public List<TodoEntity> delete(final TodoEntity entity) {
        validate(entity);

        try {
            todoRepository.delete(entity);
        } catch (Exception e) {
            log.error("error deleting entity", entity.getId(), e);

            throw  new RuntimeException("error deleting entity " + entity.getId());
        }

        return findAll(entity.getUserId());
    }
}
