package com.example.demo.aop;

import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckLoginAspect {

	@Autowired
	HttpSession session;

	//ログ出力処理
	//全Controllerクラスの全メソッド処理前を指定
	@Before("execution(* com.example.demo.controller.*Controller.*(..))")
	public void writeLog(JoinPoint jp) {
		Object user = session.getAttribute("user");
		//ログインしたアカウント情報を取得
		if (user == null) {
			System.out.print("ゲスト：");
		} else {
			System.out.print(user + "：");
		}
		System.out.println(jp.getSignature());
	}

	//未ログインの場合ログインページにリダイレクト
	@Around("execution(@org.springframework.web.bind.annotation.GetMapping * com.example.demo.controller.TaskController.*(..)) ||"
			+ "execution(@org.springframework.web.bind.annotation.PostMapping * com.example.demo.controller.TaskController.*(..))")
	public Object checkLogin(ProceedingJoinPoint jp) throws Throwable {
		Object user = session.getAttribute("user");
		if (user == null) {
			System.err.println("ログインしていません！");
			//リダイレクト先を指定する
			//パラメータを渡すことでログインControllerで
			//個別のメッセージをThymeleafに渡すことも可能
			return "redirect:/login?error=notLoggedIn";
		}
		//Controller内のメソッドを実行
		return jp.proceed();
	}
}
