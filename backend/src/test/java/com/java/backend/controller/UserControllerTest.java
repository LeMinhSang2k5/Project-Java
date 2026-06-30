package com.java.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.entity.User;
import com.java.backend.enums.Role;
import com.java.backend.exception.UserNotFoundException;
import com.java.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setFullName("Test User");
        testUser.setRole(Role.STUDENT); // Assuming Role enum exists
    }

    // 1. newUser Tests

    @Test
    void newUser_Success() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    void newUser_EmailNull() throws Exception {
        testUser.setEmail(null);
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email không được để trống")));
    }

    @Test
    void newUser_EmailEmpty() throws Exception {
        testUser.setEmail("   ");
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email không được để trống")));
    }

    @Test
    void newUser_EmailInvalid() throws Exception {
        testUser.setEmail("invalid-email");
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email không đúng định dạng")));
    }

    @Test
    void newUser_PasswordNullOrEmpty() throws Exception {
        testUser.setPassword("");
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Mật khẩu không được để trống")));
    }

    @Test
    void newUser_FullNameNullOrEmpty() throws Exception {
        testUser.setFullName("");
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Họ tên không được để trống")));
    }

    @Test
    void newUser_RoleNull() throws Exception {
        testUser.setRole(null);
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Vai trò không được để trống")));
    }

    @Test
    void newUser_EmailDuplicate() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email đã tồn tại trong hệ thống")));
    }

    @Test
    void newUser_Exception() throws Exception {
        when(userRepository.findByEmail(anyString())).thenThrow(new RuntimeException("DB error"));
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Lỗi khi tạo user")));
    }

    // 2. getAllUsers Tests

    @Test
    void getAllUsers_Success() throws Exception {
        Page<User> page = new PageImpl<>(Arrays.asList(testUser));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalItems", is(1)));
    }

    @Test
    void getAllUsers_NegativePage() throws Exception {
        mockMvc.perform(get("/api/users?page=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Số trang phải lớn hơn hoặc bằng 0")));
    }

    @Test
    void getAllUsers_Exception() throws Exception {
        when(userRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/api/users?page=0"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("Lỗi khi lấy danh sách user")));
    }

    // 3. getUserById Tests

    @Test
    void getUserById_Success() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_Exception() throws Exception {
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("Error"));
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("Lỗi khi lấy thông tin user")));
    }

    // 4. updateUser Tests

    @Test
    void updateUser_SuccessFullUpdate() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updateRequest = new User();
        updateRequest.setEmail("new@example.com");
        updateRequest.setPassword("newpass");
        updateRequest.setFullName("New Name");
        updateRequest.setRole(Role.ADMIN);

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_SuccessPartialUpdate() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updateRequest = new User();
        // Only update password and role (no email/fullname to trigger bypass branches)
        updateRequest.setPassword("newpass");
        updateRequest.setRole(Role.ADMIN);

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_EmailEmpty() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User updateRequest = new User();
        updateRequest.setEmail("   ");

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email không được để trống")));
    }

    @Test
    void updateUser_EmailInvalidFormat() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User updateRequest = new User();
        updateRequest.setEmail("invalid");

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email không đúng định dạng")));
    }

    @Test
    void updateUser_EmailDuplicate() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        User otherUser = new User();
        otherUser.setId(2L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(otherUser));

        User updateRequest = new User();
        updateRequest.setEmail("other@example.com");

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email đã tồn tại trong hệ thống")));
    }
    
    @Test
    void updateUser_EmailSameUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        
        User sameUser = new User();
        sameUser.setId(1L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(sameUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updateRequest = new User();
        updateRequest.setEmail("same@example.com");

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_FullNameEmpty() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User updateRequest = new User();
        updateRequest.setFullName("   ");

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Họ tên không được để trống")));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_Exception() throws Exception {
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("DB Error"));

        mockMvc.perform(put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("Lỗi khi cập nhật user")));
    }

    // 5. deleteUser Tests

    @Test
    void deleteUser_Success() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("thành công")));
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_Exception() throws Exception {
        when(userRepository.existsById(1L)).thenThrow(new RuntimeException("DB Error"));

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("Lỗi khi xóa user")));
    }
}
