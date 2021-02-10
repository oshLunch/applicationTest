package com.cos.test.web;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.test.domain.Book;
import com.cos.test.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class BookController {
	
	private final BookRepository bookRepository;
	
	@Transactional
	@PostMapping("/book")
	public Book save(@RequestBody Book book) {
		return bookRepository.save(book);		
	}
	
	@Transactional(readOnly = true)
	@GetMapping("/book")
	public List<Book> findAll() {
//		List<Book> books = bookRepository.findAll();
//		System.out.println("배열 사이즈는" + books.size());
		return bookRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	@GetMapping("/book/{id}")
	public Book findById(@PathVariable int id) {
		return bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요!"));
		
	}
	
	@Transactional
	@PutMapping("/book/{id}")
	public Book update(@PathVariable int id, @RequestBody Book book) {
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요!"));
		bookEntity.setPrice(book.getPrice());
		bookEntity.setRating(book.getRating());
		bookEntity.setTitle(book.getTitle());
		
		return bookEntity;
	}
	
	@Transactional
	@DeleteMapping("/book/{id}")
	public Book delete(@PathVariable int id) {
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요!"));
		
		bookRepository.deleteById(id);
		
		return bookEntity;		
	}
	
	

}
