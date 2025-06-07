package com.java.backend;

import com.java.backend.entity.Student;
import com.java.backend.enums.Gender;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class BackendApplication {

// 	public static void main(String[] args) {
// 		SpringApplication.run(BackendApplication.class, args);
// 	}

// }

import com.java.backend.repository.StudentRepository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {

	@Autowired
	private StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("✅ App đang chạy...");

		// Tạo và lưu 1 student test
		Student student = new Student("Nguyen Van Linh", "linh123@gmail.com", LocalDate.of(2000, 1, 1), Gender.MALE,
				"12A5");

		studentRepository.save(student);

		System.out.println("🎉 Đã thêm sinh viên test vào DB!");
	}
}