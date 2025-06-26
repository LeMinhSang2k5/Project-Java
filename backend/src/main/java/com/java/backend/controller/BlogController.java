package com.java.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backend.entity.Blog;
import com.java.backend.service.BlogService;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        try {
            List<Blog> blogs = blogService.getAllBlogs();
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi lấy danh sách blog: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable Long id) {
        try {
            Optional<Blog> blog = blogService.getBlogById(id);
            if (blog.isPresent()) {
                return ResponseEntity.ok(blog.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Không tìm thấy blog với ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi lấy thông tin blog: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody Blog blog) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (blog.getTitle() == null || blog.getTitle().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Tiêu đề không được để trống");
                return ResponseEntity.badRequest().body(error);
            }
            if (blog.getContent() == null || blog.getContent().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Nội dung không được để trống");
                return ResponseEntity.badRequest().body(error);
            }
            if (blog.getAuthor() == null || blog.getAuthor().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Tác giả không được để trống");
                return ResponseEntity.badRequest().body(error);
            }
            if (blog.getCategory() == null || blog.getCategory().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Danh mục không được để trống");
                return ResponseEntity.badRequest().body(error);
            }

            Blog createdBlog = blogService.createBlog(blog);
            return ResponseEntity.ok(createdBlog);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi tạo blog: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (blog.getTitle() == null || blog.getTitle().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Tiêu đề không được để trống");
                return ResponseEntity.badRequest().body(error);
            }
            if (blog.getContent() == null || blog.getContent().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Nội dung không được để trống");
                return ResponseEntity.badRequest().body(error);
            }

            Blog updatedBlog = blogService.updateBlog(id, blog);
            return ResponseEntity.ok(updatedBlog);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Không tìm thấy blog với ID: " + id);
                return ResponseEntity.notFound().build();
            }
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi cập nhật blog: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi cập nhật blog: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        try {
            // Kiểm tra blog có tồn tại không
            Optional<Blog> existingBlog = blogService.getBlogById(id);
            if (existingBlog.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Không tìm thấy blog với ID: " + id);
                return ResponseEntity.notFound().build();
            }

            blogService.deleteBlog(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Xóa blog thành công");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi xóa blog: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
