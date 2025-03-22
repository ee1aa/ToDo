package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
	// ユーザーIDでタスクを取得
	List<Task> findByUserId(Integer userId);

	// ユーザーIDとカテゴリIDでタスクを取得
	List<Task> findByUserIdAndCategoryId(Integer userId, Integer categoryId);
}
