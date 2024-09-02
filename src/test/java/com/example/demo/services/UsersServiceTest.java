package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mockito;

@SpringBootTest
@AutoConfigureMockMvc
class UsersServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;

    @Test
    void testCreateUser() throws Exception {
        Mockito.doNothing().when(usersService).create(any());
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isCreated());
    }

    @Test
    void listUserShouldReturnTwoUsers() throws Exception {
        List<User> expectedList = createUsers(2);
        Mockito.when(usersService.getAllUsers(anyInt())).thenReturn(expectedList);
        MvcResult mvcResult = mockMvc.perform(get("/users")
                .param("limit", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<User> users = Arrays.asList(objectMapper.readValue(jsonResponse, User[].class));

        assertEquals(2, users.size());
        assertIterableEquals(expectedList, users);
    }

    @Test
    void testShowUserById_UserFound() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");

        Mockito.when(usersService.getUser(anyLong())).thenReturn(user);

        mockMvc.perform(get("/users/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    private List<User> createUsers(int quantity){
        List<User> users = new ArrayList<>();
        for(long i = 0; i < quantity; i++) {
            User u = new User();
            u.id(i);
            u.name("test"+i);
            users.add(u);
        }
        return users;
    }
}