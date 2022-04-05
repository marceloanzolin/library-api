package com.anzolinmarcelo.livariaapi.resource;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.anzolinmarcelo.livariaapi.dto.BookDTO;
import com.anzolinmarcelo.livariaapi.exception.ApiErros;
import com.anzolinmarcelo.livariaapi.exception.BusinessException;
import com.anzolinmarcelo.livariaapi.model.entity.Book;
import com.anzolinmarcelo.livariaapi.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	private BookService service;
	private ModelMapper modelMapper;
	
	public BookController(BookService service,ModelMapper mapper) {
		this.service = service;
		this.modelMapper = mapper;
	}

	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public BookDTO create(@RequestBody  @Valid BookDTO dto) {
		
		Book entity = modelMapper.map(dto, Book.class);
		
		entity = service.save(entity);
		
		return modelMapper.map(entity, BookDTO.class);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		return new ApiErros(bindingResult);
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErros handleBusinessExceptions(BusinessException ex) {
		return new ApiErros(ex);
	}
	
	
}
