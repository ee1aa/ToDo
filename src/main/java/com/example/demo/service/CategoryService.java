package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> findAll() {
		// すべてのカテゴリをデータベースから取得
		return categoryRepository.findAll();
	}

	// 他のカテゴリ関連のビジネスメソッドを追加できます
}
