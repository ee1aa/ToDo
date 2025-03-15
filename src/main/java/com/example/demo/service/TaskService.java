package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public Task findById(Integer taskId) {
		// タスクIDに基づいてタスクをデータベースから取得
		return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
	}
}
