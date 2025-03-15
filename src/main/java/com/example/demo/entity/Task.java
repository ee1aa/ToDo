package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //タスクIDの自動採番
	private Integer id; //タスクID

	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id") //categoriesテーブルのidと紐付け
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id") // user_id と users.id を結びつけ
	private User user; // ユーザーエンティティへの参照

	private String title;

	@Column(name = "closing_date")
	private LocalDate closingDate; //期限

	private Integer progress; // 進捗状況

	@Column(name = "memo", columnDefinition = "TEXT")
	private String memo; // メモ

	//コンストラクタ
	public Task() {
	}

	//新規作成用コンストラクタ
	public Task(Category category, User user, String title, LocalDate closingDate, Integer progress, String memo) {
		this.category = category;
		this.user = user;
		this.title = title;
		this.closingDate = closingDate;
		this.progress = progress;
		this.memo = memo;
	}

	//初期データ用コンストラクタ
	public Task(Integer id, Category category, User user, String title, LocalDate closingDate, Integer progress,
			String memo) {
		this.id = id;
		this.category = category;
		this.user = user;
		this.title = title;
		this.closingDate = closingDate;
		this.progress = progress;
		this.memo = memo;
	}

	//ゲッター
	public Integer getId() {
		return id;
	}

	public Category getCategory() {
		return category;
	}

	public User getUser() {
		return user;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getClosingDate() {
		return closingDate;
	}

	public Integer getProgress() {
		return progress;
	}

	public String getMemo() {
		return memo;
	}
}
