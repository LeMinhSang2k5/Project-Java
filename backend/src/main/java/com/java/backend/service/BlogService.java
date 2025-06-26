package com.java.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.java.backend.repository.BlogRepository;
import com.java.backend.entity.Blog;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Lấy tất cả các blog
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // Lấy blog theo ID
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    private Long findSmallestAvailableId() {
        List<Long> existingIds = blogRepository.findAll().stream()
                .map(Blog::getId)
                .sorted()
                .collect(Collectors.toList());

        Long smallestId = 1L;
        for (Long existingId : existingIds) {
            if (existingId.equals(smallestId)) {
                smallestId++;
            } else if (existingId > smallestId) {
                break;
            }
        }
        return smallestId;
    }

    @Transactional
    public Blog createBlog(Blog blog) {
        // Tìm ID nhỏ nhất chưa được sử dụng
        Long newId = findSmallestAvailableId();

        // Reset sequence nếu cần
        entityManager.createNativeQuery(
                "ALTER TABLE blogs AUTO_INCREMENT = ?")
                .setParameter(1, newId)
                .executeUpdate();

        // Đặt ID mới cho blog
        blog.setId(null); // Để cho phép JPA tạo ID mới
        Blog savedBlog = blogRepository.save(blog);

        // Nếu ID được tạo lớn hơn mong đợi, thử lại với ID mong muốn
        if (savedBlog.getId() > newId) {
            blog.setId(newId);
            savedBlog = blogRepository.save(blog);
        }

        return savedBlog;
    }

    public Blog updateBlog(Long id, Blog blogDetails) {
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
