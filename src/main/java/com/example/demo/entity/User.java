package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //ユーザーIDの自動採番
	private Integer id; //ユーザーID

	private String email; //メールアドレス

	private String name; //名前

	private String password; //パスワード

	//コンストラクタ
	public User() {
	}

	//新規登録用コンストラクタ
	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

	//ゲッター
	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
