package com.cos.test.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.test.domain.Book;
import com.cos.test.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerItegreTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	private void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
		bookRepository.deleteAll();
	}

	@Test
	public void saveTest() throws Exception{
		//given
		Book book = new Book(null, "제목1", 1.3, 4.1);
		String content = new ObjectMapper().writeValueAsString(book);
		
		ResultActions resultActions = mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultActions.andExpect(jsonPath("$.title").value("제목1"))
		.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void findAllTest() throws Exception {
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목1", 3.1, 4.3));
		books.add(new Book(null, "제목2", 3.1, 4.3));
		books.add(new Book(null, "제목3", 3.1, 4.3));
		bookRepository.saveAll(books);
		
		ResultActions resultActions = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultActions.andExpect(jsonPath("$.[0].title").value("제목1"))
		.andExpect(jsonPath("$", Matchers.hasSize(3)))//뭐지? junit5이 내장돼 있음에도 불구하고 내가 라이브러리를 또 추가시켜줬기 때문! build패스에서 제거
		.andDo(MockMvcResultHandlers.print());
		
	}
	
	
	
	@Test
	public void findByIdTest() throws Exception{
		int id = 2;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목1", 3.1, 4.3));
		books.add(new Book(null, "제목2", 3.1, 4.3));
		books.add(new Book(null, "제목3", 3.1, 4.3));
		bookRepository.saveAll(books);
		
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("제목2"))
		.andDo(MockMvcResultHandlers.print());	
	}
	
	
	@Test
	public void updateTest() throws Exception {
		int id = 2;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목1", 3.1, 4.3));
		books.add(new Book(null, "제목2", 3.1, 4.3));
		bookRepository.saveAll(books);
		
		Book book = new Book(null, "수정된제목", 1.3, 4.1);
		String content = new ObjectMapper().writeValueAsString(book);
			
		ResultActions resultAction = mockMvc
				.perform(put("/book/{id}", id)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(content) 																											
						.accept(MediaType.APPLICATION_JSON_UTF8));
		
		resultAction.andExpect(jsonPath("$.title").value("수정된제목"))
		.andDo(MockMvcResultHandlers.print());	
		
		
	}
	
	
	@Test
	public void deleteTest() throws Exception{
		int id = 2;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "제목1", 3.1, 4.3));
		books.add(new Book(null, "제목2", 3.1, 4.3));
		bookRepository.saveAll(books);
		
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id));
		
		resultAction.andExpect(jsonPath("$.title").value("제목2"))
		.andDo(MockMvcResultHandlers.print());	
		
		
	}
}
