package com.java.backend.service;

import com.java.backend.entity.Blog;
import com.java.backend.repository.BlogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    @Test
    void getAllBlogs_shouldReturnRepositoryResult() {
        List<Blog> expected = List.of(sampleBlog());
        when(blogRepository.findAll()).thenReturn(expected);

        List<Blog> result = blogService.getAllBlogs();

        assertSame(expected, result);
        verify(blogRepository).findAll();
    }

    @Test
    void getBlogById_shouldReturnRepositoryResult() {
        Blog blog = sampleBlog();
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));

        Optional<Blog> result = blogService.getBlogById(1L);

        assertTrue(result.isPresent());
        assertSame(blog, result.get());
        verify(blogRepository).findById(1L);
    }

    @Test
    void createBlog_shouldSaveBlogWithNullId() {
        Blog input = sampleBlog();
        input.setId(10L);
        when(blogRepository.save(any(Blog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Blog result = blogService.createBlog(input);

        assertNotNull(result);
        assertNull(result.getId());
        ArgumentCaptor<Blog> captor = ArgumentCaptor.forClass(Blog.class);
        verify(blogRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
    }

    @Test
    void createBlog_shouldThrowForNullInput() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> blogService.createBlog(null));

        assertEquals("Blog cannot be null", exception.getMessage());
        verify(blogRepository, never()).save(any(Blog.class));
    }

    @Test
    void createBlog_shouldWrapRepositoryException() {
        Blog input = sampleBlog();
        when(blogRepository.save(any(Blog.class))).thenThrow(new RuntimeException("db down"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> blogService.createBlog(input));

        assertTrue(exception.getMessage().contains("Lỗi khi tạo blog"));
        assertEquals("db down", exception.getCause().getMessage());
    }

    @Test
    void updateBlog_shouldUpdateExistingBlogAndSave() {
        Blog existingBlog = sampleBlog();
        existingBlog.setId(1L);
        existingBlog.setTitle("Old title");
        existingBlog.setContent("Old content");
        existingBlog.setAuthor("Old author");
        existingBlog.setCategory("Old category");
        existingBlog.setThumbnail("Old thumbnail");

        Blog updatePayload = sampleBlog();
        updatePayload.setTitle("New title");
        updatePayload.setContent("New content");
        updatePayload.setAuthor("New author");
        updatePayload.setCategory("New category");
        updatePayload.setThumbnail("New thumbnail");

        when(blogRepository.findById(1L)).thenReturn(Optional.of(existingBlog));
        when(blogRepository.save(any(Blog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Blog result = blogService.updateBlog(1L, updatePayload);

        assertEquals(1L, result.getId());
        assertEquals("New title", result.getTitle());
        assertEquals("New content", result.getContent());
        assertEquals("New author", result.getAuthor());
        assertEquals("New category", result.getCategory());
        assertEquals("New thumbnail", result.getThumbnail());
        verify(blogRepository).save(existingBlog);
    }

    @Test
    void updateBlog_shouldThrowWhenBlogNotFound() {
        when(blogRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> blogService.updateBlog(99L, sampleBlog()));

        assertTrue(exception.getMessage().contains("Blog not found with id 99"));
        verify(blogRepository, never()).save(any(Blog.class));
    }

    @Test
    void deleteBlog_shouldDelegateToRepository() {
        blogService.deleteBlog(7L);

        verify(blogRepository).deleteById(7L);
    }

    @Test
    void updateBlog_shouldThrowWhenIdNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> blogService.updateBlog(null, sampleBlog()));
        assertEquals("Blog id cannot be null", exception.getMessage());
    }

    @Test
    void updateBlog_shouldThrowWhenDetailsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> blogService.updateBlog(1L, null));
        assertEquals("Blog details cannot be null", exception.getMessage());
    }

    private Blog sampleBlog() {
        Blog blog = new Blog();
        blog.setTitle("Title");
        blog.setContent("Content");
        blog.setAuthor("Author");
        blog.setCategory("Category");
        blog.setThumbnail("thumbnail.jpg");
        blog.setPublishDate(LocalDate.of(2024, 1, 1));
        blog.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0));
        blog.setUpdatedAt(LocalDateTime.of(2024, 1, 1, 10, 30));
        return blog;
    }
}
