package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

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
			Model model) {
		//categoryテーブルから全カテゴリー一覧を取得
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);

		//商品一覧情報の取得
		List<Task> taskList = null;
		if (categoryId == null) {
			taskList = taskRepository.findAll();
		} else {
			//itemsテーブルをカテゴリーIDを指定して一覧を取得
			taskList = taskRepository.findByCategoryId(categoryId);
		}
		model.addAttribute("items", taskList);

		return "tasks";
	}

	//タスク新規作成フォームの表示
	@GetMapping("/createTask")
	public String create() {
		return "createTask";
	}

	//新規作成処理、タスク一覧へリダイレクト
	@PostMapping("/createTask")
	public String add(
			@RequestParam(value = "categoryId", defaultValue = "") Category categoryId,
			@RequestParam(value = "userId", defaultValue = "") User userId,
			@RequestParam(value = "title", defaultValue = "") String title,
			@RequestParam(value = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam(value = "progress", defaultValue = "") Integer progress,
			@RequestParam(value = "memo", defaultValue = "") String memo,
			Model model) {

		//Taskオブジェクトの生成
		Task task = new Task(categoryId, userId, title, closingDate, progress, memo);
		//tasksテーブルへの反映（INSERT）
		taskRepository.save(task);
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
			@RequestParam(value = "categoryId", defaultValue = "") Category categoryId,
			@RequestParam(value = "userId", defaultValue = "") User userId,
			@RequestParam(value = "title", defaultValue = "") String title,
			@RequestParam(value = "closingDate", defaultValue = "") LocalDate closingDate,
			@RequestParam(value = "progress", defaultValue = "") Integer progress,
			@RequestParam(value = "memo", defaultValue = "") String memo,
			Model model) {

		//Taskオブジェクトの生成
		Task task = new Task(id, categoryId, userId, title, closingDate, progress, memo);
		//tasksテーブルへの反映（INSERT）
		taskRepository.save(task);
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
