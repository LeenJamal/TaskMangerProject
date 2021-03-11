package com.spring.taskmanger.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.spring.taskmanger.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

	Page<Task> findByUserId(Long id, Pageable pageable);

	Optional<Task> findByIdAndUserId(Long id, Long userId);

}
