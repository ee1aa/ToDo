package com.example.demo.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.entity.User;
import com.example.demo.model.Account;
import com.example.demo.repository.UserRepository;

@Controller
@SessionAttributes("user")

public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired

	HttpSession session;

	@Autowired

	UserRepository userRepository;

	@Autowired

	Account account;

	// ログイン画面を表示
	@GetMapping({ "/", "/login" })

	public String index(@RequestParam(name = "error", defaultValue = "") String error, Model model) {

		// ログインエラーメッセージ処理
		if ("notLoggedIn".equals(error)) {
			model.addAttribute("message", "ログインしてください");
		}

		return "login";
	}

	// ログアウト処理
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if (session != null) {
			session.invalidate(); // セッションを無効化
		}
		return "redirect:/login?error=logout";
	}

	// ログインを実行
	@PostMapping("/login")

	public String login(

			@RequestParam("email") String email,

			@RequestParam("password") String password,

			Model model) {

		logger.info("ログイン処理開始");

		logger.debug("入力されたメール: {}", email);

		logger.debug("入力されたパスワード: {}", password);

		// メール・パスワードが未入力の場合

		if (email == null || email.isEmpty() || password == null || password.isEmpty()) {

			model.addAttribute("message", "メールアドレスとパスワードを入力してください");

			logger.warn("メールアドレスまたはパスワードが未入力");

			return "login";

		}

		try {

			// ユーザー情報を取得

			Optional<User> userOptional = userRepository.findByEmail(email);
			User user = userOptional.get();
			logger.debug("取得したユーザー: {}", user);

			// メールが登録されていない、またはパスワードが不一致の場合

			if (user == null || !user.getPassword().equals(password)) {

				model.addAttribute("message", "メールアドレスとパスワードを正しく入力してください");

				logger.warn("ログイン失敗: メールアドレスまたはパスワードが不一致");

				return "login";

			}

			// セッションにユーザー情報を保存

			session.setAttribute("user", user);

			logger.info("ログイン成功: ユーザー情報をセッションに保存 ({})", user.getEmail());

			// セッションに保存されているか確認

			User sessionUser = (User) session.getAttribute("user");

			if (sessionUser == null) {

				logger.error("セッションにユーザーが保存されていません！");

			} else {

				logger.debug("セッションから取得したユーザー: {}", sessionUser.getEmail());

			}
			model.addAttribute("user", user);
			session.setAttribute("userId", user.getId());
			return "redirect:/tasks";

		} catch (Exception e) {

			logger.error("ログイン処理中にエラーが発生", e);

			model.addAttribute("message", "システムエラーが発生しました");

			return "login";

		}

	}

	// ユーザー登録フォームの表示

	@GetMapping("/createUser")

	public String create() {

		logger.info("ユーザー登録画面を表示");

		return "createUser";

	}

	// ユーザー登録処理、ログイン画面にリダイレクト

	@PostMapping("/createUser")

	public String add(

			@RequestParam("email") String email,

			@RequestParam("name") String name,

			@RequestParam("password") String password,

			@RequestParam("confirmPassword") String confirmPassword,

			Model model) {

		logger.info("ユーザー登録処理開始");

		logger.debug("入力されたメール: {}, 名前: {}", email, name);

		// パスワードと確認用パスワードが一致しない場合

		if (!password.equals(confirmPassword)) {

			model.addAttribute("message", "パスワードが一致しません");

			logger.warn("パスワードが一致しません: {} vs {}", password, confirmPassword);

			return "createUser"; // 登録画面に戻る

		}

		// メアドとパスワードが空エラー

		if (email == null || email.isEmpty() || password == null || password.isEmpty()) {

			model.addAttribute("message", "メールアドレスとパスワードを入力してください");

			logger.warn("登録時のメールアドレスまたはパスワードが未入力");

			return "createUser";

		}

		try {

			// Userオブジェクトの生成

			User user = new User(email, name, password);

			logger.debug("新規ユーザー作成: {}", user);

			// Usersテーブルへの反映（INSERT）

			userRepository.save(user);

			logger.info("ユーザー登録成功: {}", email);

			// /login へリダイレクト

			return "redirect:/login";

		} catch (Exception e) {

			logger.error("ユーザー登録処理中にエラーが発生", e);

			model.addAttribute("message", "システムエラーが発生しました");

			return "login";

		}

	}

}