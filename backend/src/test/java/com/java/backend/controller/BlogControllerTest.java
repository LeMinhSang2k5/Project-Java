package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.Blog;
import com.java.backend.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlogService blogService;

    @Test
    void getAllBlogs_shouldReturn200AndList() throws Exception {
        when(blogService.getAllBlogs()).thenReturn(List.of(sampleBlog()));

        mockMvc.perform(get("/api/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test title"));
    }

    @Test
    void getAllBlogs_shouldReturn500WhenServiceFails() throws Exception {
        when(blogService.getAllBlogs()).thenThrow(new RuntimeException("db down"));

        mockMvc.perform(get("/api/blogs"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Lỗi khi lấy danh sách blog: db down"));
    }

    @Test
    void getBlogById_shouldReturn404WhenNotFound() throws Exception {
        when(blogService.getBlogById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/blogs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBlogById_shouldReturn500WhenServiceFails() throws Exception {
        when(blogService.getBlogById(1L)).thenThrow(new RuntimeException("db down"));

        mockMvc.perform(get("/api/blogs/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Lỗi khi lấy thông tin blog: db down"));
    }

    @Test
    void createBlog_shouldReturn201ForValidInput() throws Exception {
        Blog blog = sampleBlog();
        when(blogService.createBlog(any(Blog.class))).thenReturn(blog);

        mockMvc.perform(post("/api/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test title"));
    }

    @Test
    void createBlog_shouldReturn400ForEmptyTitle() throws Exception {
        Blog blog = sampleBlog();
        blog.setTitle("   ");

        mockMvc.perform(post("/api/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tiêu đề không được để trống"));
    }

    @Test
    void createBlog_shouldReturn400ForEmptyAuthor() throws Exception {
        Blog blog = sampleBlog();
        blog.setAuthor("   ");

        mockMvc.perform(post("/api/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tác giả không được để trống"));
    }

    @Test
    void createBlog_shouldReturn400ForNullBody() throws Exception {
        mockMvc.perform(post("/api/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Body không được để trống"));
    }

    @Test
    void createBlog_shouldReturn400ForEmptyContent() throws Exception {
        Blog blog = sampleBlog();
        blog.setContent("   ");

        mockMvc.perform(post("/api/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Nội dung không được để trống"));
    }

    @Test
    void createBlog_shouldReturn400ForEmptyCategory() throws Exception {
        Blog blog = sampleBlog();
        blog.setCategory("   ");

        mockMvc.perform(post("/api/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Danh mục không được để trống"));
    }

    @Test
    void updateBlog_shouldReturn200WhenUpdated() throws Exception {
        Blog blog = sampleBlog();
        when(blogService.updateBlog(eq(1L), any(Blog.class))).thenReturn(blog);

        mockMvc.perform(put("/api/blogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test title"));
    }

    @Test
    void updateBlog_shouldReturn400ForEmptyContent() throws Exception {
        Blog blog = sampleBlog();
        blog.setContent("   ");

        mockMvc.perform(put("/api/blogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Nội dung không được để trống"));
    }

    @Test
    void updateBlog_shouldReturn400ForEmptyTitle() throws Exception {
        Blog blog = sampleBlog();
        blog.setTitle("   ");

        mockMvc.perform(put("/api/blogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Tiêu đề không được để trống"));
    }

    @Test
    void updateBlog_shouldReturn400ForNullBody() throws Exception {
        mockMvc.perform(put("/api/blogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Body không được để trống"));
    }

    @Test
    void updateBlog_shouldReturn404WhenServiceThrowsNotFound() throws Exception {
        when(blogService.updateBlog(eq(1L), any(Blog.class)))
                .thenThrow(new RuntimeException("Blog not found with id 1"));

        mockMvc.perform(put("/api/blogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBlog())))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBlog_shouldReturn500WhenServiceFails() throws Exception {
        when(blogService.updateBlog(eq(1L), any(Blog.class))).thenThrow(new RuntimeException("db down"));

        mockMvc.perform(put("/api/blogs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBlog())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Lỗi khi cập nhật blog: db down"));
    }

    @Test
    void deleteBlog_shouldReturn200AndSuccessMessage() throws Exception {
        when(blogService.getBlogById(1L)).thenReturn(Optional.of(sampleBlog()));
        doNothing().when(blogService).deleteBlog(1L);

        mockMvc.perform(delete("/api/blogs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Xóa blog thành công"));
    }

    @Test
    void deleteBlog_shouldReturn404WhenNotFound() throws Exception {
        when(blogService.getBlogById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/blogs/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBlog_shouldReturn500WhenServiceFails() throws Exception {
        when(blogService.getBlogById(1L)).thenThrow(new RuntimeException("db down"));

        mockMvc.perform(delete("/api/blogs/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Lỗi khi xóa blog: db down"));
    }

    private Blog sampleBlog() {
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Test title");
        blog.setContent("Test content");
        blog.setAuthor("Test author");
        blog.setCategory("Test category");
        blog.setThumbnail("thumb.jpg");
        blog.setPublishDate(LocalDate.of(2024, 1, 1));
        blog.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));
        blog.setUpdatedAt(LocalDateTime.of(2024, 1, 1, 10, 30));
        return blog;
    }
}
