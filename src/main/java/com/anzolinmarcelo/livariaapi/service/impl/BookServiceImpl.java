package com.anzolinmarcelo.livariaapi.service.impl;

import org.springframework.stereotype.Service;

import com.anzolinmarcelo.livariaapi.exception.BusinessException;
import com.anzolinmarcelo.livariaapi.model.entity.Book;
import com.anzolinmarcelo.livariaapi.model.repository.BookRepository;
import com.anzolinmarcelo.livariaapi.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private BookRepository repository;
	
	public BookServiceImpl(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {
		if(repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn já cadastrada");
		} 
		return repository.save(book);
	}

}
