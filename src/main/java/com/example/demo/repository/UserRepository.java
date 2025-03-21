package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	//emailでログインできるユーザーを検索
	Optional<User> findByEmail(String email);

	//新規登録用通信
	void save(com.example.demo.model.Account user);

}
