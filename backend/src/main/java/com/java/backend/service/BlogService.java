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
    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }
    public Blog BlogfindById(Long id, Blog blogDetails) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id " + id));

        return blogRepository.save(blog);
    }
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }
}
