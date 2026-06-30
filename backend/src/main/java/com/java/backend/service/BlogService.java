package com.java.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.java.backend.repository.BlogRepository;
import com.java.backend.entity.Blog;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    // Lấy tất cả các blog
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // Lấy blog theo ID
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    // Tạo blog mới (đơn giản hóa)
    public Blog createBlog(Blog blog) {
        if (blog == null) {
            throw new IllegalArgumentException("Blog cannot be null");
        }
        try {
            // Đặt ID về null để JPA tự động tạo ID
            blog.setId(null);

            // Lưu blog với ID tự động
            return blogRepository.save(blog);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo blog: " + e.getMessage(), e);
        }
    }

    public Blog updateBlog(Long id, Blog blogDetails) {
        if (id == null) {
            throw new IllegalArgumentException("Blog id cannot be null");
        }
        if (blogDetails == null) {
            throw new IllegalArgumentException("Blog details cannot be null");
        }
        Blog existingBlog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id " + id));

        // Cập nhật các trường của blog hiện có
        existingBlog.setTitle(blogDetails.getTitle());
        existingBlog.setContent(blogDetails.getContent());
        existingBlog.setAuthor(blogDetails.getAuthor());
        existingBlog.setCategory(blogDetails.getCategory());
        existingBlog.setThumbnail(blogDetails.getThumbnail());

        // Giữ nguyên các trường thời gian quan trọng
        // Không cập nhật createdAt vì nó được đánh dấu là updatable=false
        // Không cập nhật publishDate để giữ ngày xuất bản gốc

        // updatedAt sẽ tự động được cập nhật bởi @UpdateTimestamp

        // Đảm bảo sử dụng ID hiện có
        existingBlog.setId(id);

        return blogRepository.save(existingBlog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
