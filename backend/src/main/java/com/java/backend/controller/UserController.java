package com.java.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.backend.entity.User;
import com.java.backend.exception.UserNotFoundException;
import com.java.backend.repository.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    ResponseEntity<?> newUser(@RequestBody User newUser) {
        try {
            // Validate required fields
            if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email không được để trống"));
            }

            String email = newUser.getEmail().trim();
            if (!isValidEmailFormat(email)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email không đúng định dạng"));
            }

            if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Mật khẩu không được để trống"));
            }
            if (newUser.getFullName() == null || newUser.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Họ tên không được để trống"));
            }
            if (newUser.getRole() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Vai trò không được để trống"));
            }

            // Check if email already exists
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email đã tồn tại trong hệ thống"));
            }

            // Normalize and save user
            newUser.setEmail(email);
            newUser.setFullName(newUser.getFullName().trim());
            User savedUser = userRepository.save(newUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Lỗi khi tạo user: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page) {
        try {
            if (page < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Số trang phải lớn hơn hoặc bằng 0"));
            }

            Pageable pageable = PageRequest.of(page, 5, Sort.by("id").ascending());
            Page<User> userPage = userRepository.findAll(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("content", userPage.getContent());
            response.put("currentPage", userPage.getNumber());
            response.put("pageSize", userPage.getSize());
            response.put("totalItems", userPage.getTotalElements());
            response.put("totalPages", userPage.getTotalPages());
            response.put("isLast", userPage.isLast());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi khi lấy danh sách user: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{id}")
    ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi khi lấy thông tin user: " + e.getMessage()));
        }
    }

    private boolean isValidEmailFormat(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @PutMapping("/user/{id}")
    ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable Long id) {
        try {
            User updatedUser = userRepository.findById(id)
                    .map(user -> {
                        // Chỉ cập nhật các trường không null
                        if (newUser.getPassword() != null) {
                            user.setPassword(newUser.getPassword());
                        }
                        if (newUser.getEmail() != null) {
                            String email = newUser.getEmail().trim();

                            if (email.isEmpty()) {
                                throw new IllegalArgumentException("Email không được để trống");
                            }

                            if (!isValidEmailFormat(email)) {
                                throw new IllegalArgumentException("Email không đúng định dạng");
                            }

                            Optional<User> existingByEmail = userRepository.findByEmail(email);
                            if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(id)) {
                                throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
                            }

                            user.setEmail(email);
                        }
                        if (newUser.getFullName() != null) {
                            user.setFullName(newUser.getFullName().trim());
                        }
                        if (newUser.getRole() != null) {
                            user.setRole(newUser.getRole());
                        }
                        return userRepository.save(user);
                    })
                    .orElseThrow(() -> new UserNotFoundException(id));

            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi khi cập nhật user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/user/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!userRepository.existsById(id)) {
                throw new UserNotFoundException(id);
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok()
                    .body(Map.of("message", "Đã xóa user " + id + " thành công"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Lỗi khi xóa user: " + e.getMessage()));
        }
    }
}
