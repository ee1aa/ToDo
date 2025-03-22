package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.TaskService;

@Controller
public class TaskController {

	@Autowired
	TaskRepository taskRepository; //tasksテーブル操作用

	@Autowired
	CategoryRepository categoryRepository; //categoryテーブル操作用

	@Autowired
	UserRepository userRepository; //usersテーブル操作用

	@Autowired
	private TaskService taskService; // タスクサービスをインジェクト

	@Autowired
	private CategoryService categoryService; // カテゴリサービスをインジェクト

	//タスク一覧の表示
	//カテゴリーの絞り込みと全タスクの表示
	@GetMapping("/tasks")
	public String index(
			@RequestParam(name = "categoryId", defaultValue = "") Integer categoryId,
			HttpSession session, // セッションを取得
			Model model) {

		// ログインユーザーIDを取得
		Integer userId = (Integer) session.getAttribute("userId");

		// セッションが無い場合はログイン画面にリダイレクト
		if (userId == null) {
			return "redirect:/login";
		}

		// カテゴリ一覧取得
		model.addAttribute("categories", categoryRepository.findAll());

		// ログインユーザーのタスクを取得
		List<Task> taskList;
		if (categoryId != null) {
			taskList = taskRepository.findByUserIdAndCategoryId(userId, categoryId);
		} else {
			taskList = taskRepository.findByUserId(userId);
		}

		model.addAttribute("tasks", taskList);
		return "tasks";
	}

	//タスク新規作成フォームの表示
	@GetMapping("/createTask")
	public String create(Model model) {
		List<Category> categories = categoryRepository.findAll(); // データベースからカテゴリを取得
		model.addAttribute("categories", categories);
		return "createTask";
	}

	//新規作成処理、タスク一覧へリダイレクト
	@PostMapping("/createTask")
	public String add(
			@RequestParam(value = "categoryId", defaultValue = "") Category categoryId,
			@RequestParam(value = "title", defaultValue = "") String title,
			@RequestParam(value = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam(value = "progress", defaultValue = "") Integer progress,
			@RequestParam(value = "memo", defaultValue = "") String memo,
			HttpSession session, // セッションを取得
			Model model) {

		// ログインユーザーIDをセッションから取得
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		// ユーザーとカテゴリを取得
		User user = userRepository.findById(userId).orElse(null);

		if (user != null && categoryId != null) {
			Task task = new Task(categoryId, user, title, closingDate, progress, memo);
			//tasksテーブルへの反映（INSERT）
			taskRepository.save(task);
		}
		//「/tasks」にGETでリクエストしなおす（リダイレクト）
		return "redirect:/tasks";
	}

	//更新画面の表示
	@GetMapping("/tasks/{id}/editTask")
	public String edit(@PathVariable Integer id, Model model) {
		Task task = taskService.findById(id); // タスクIDを基にタスクを取得
		List<Category> categories = categoryService.findAll(); // カテゴリのリストも取得
		model.addAttribute("task", task);
		model.addAttribute("categories", categories);
		return "editTask";
	}

	//更新処理、タスク一覧へリダイレクト
	@PostMapping("/tasks/{id}/editTask")
	public String update(
			@PathVariable("id") Integer id,
			@RequestParam(value = "categoryId", defaultValue = "") Integer categoryId,
			@RequestParam(value = "title", defaultValue = "") String title,
			@RequestParam(value = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam(value = "progress", defaultValue = "") Integer progress,
			@RequestParam(value = "memo", defaultValue = "") String memo,
			HttpSession session, // セッションを取得
			Model model) {

		// ログインユーザーIDを取得
		Integer userId = (Integer) session.getAttribute("userId");

		if (userId == null) {
			return "redirect:/login";
		}

		Task task = taskRepository.findById(id).orElse(null);
		if (task != null) {
			// カテゴリとユーザーを取得
			Category category = categoryRepository.findById(categoryId).orElse(null);
			User user = userRepository.findById(userId).orElse(null);

			if (category != null && user != null) {
				// タスクを更新
				task.setCategory(category);
				task.setUser(user);
				task.setTitle(title);
				task.setClosingDate(closingDate);
				task.setProgress(progress);
				task.setMemo(memo);
				//tasksテーブルへの反映（INSERT）
				taskRepository.save(task);
			}
		}

		//「/tasks」にGETでリクエストしなおす（リダイレクト）
		return "redirect:/tasks";
	}

	//削除処理、タスク一覧へリダイレクト
	@PostMapping("/tasks/{id}/delete")
	public String delete(
			@PathVariable("id") Integer id,
			Model model) {

		//タスク情報から削除
		taskRepository.deleteById(id);
		//「/tasks」へリダイレクト
		return "redirect:/tasks";

	}

}
