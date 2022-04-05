package com.anzolinmarcelo.livariaapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.anzolinmarcelo.livariaapi.exception.BusinessException;
import com.anzolinmarcelo.livariaapi.model.entity.Book;
import com.anzolinmarcelo.livariaapi.model.repository.BookRepository;
import com.anzolinmarcelo.livariaapi.service.impl.BookServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	
	BookService service;
	 //simula
	@MockBean
	BookRepository repository;
	
	//antes de cada teste
	@BeforeEach
	public void setUp() {
		this.service = new BookServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Deve Salvar um livro")
	public void saveBookTest() {
		//cenario
		Book book = createValidBook();
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
		Mockito.when(repository.save(book)).thenReturn(Book.builder().
					id(10l)
					.isbn("123")
					.author("Marcelo")
					.title("Meu Livro").build());
		
		//execucao do test
		Book savedBook = service.save(book);
		
		//verificacao
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("Meu Livro");
		assertThat(savedBook.getAuthor()).isEqualTo("Marcelo");
		
		
	}

	
	@Test
	@DisplayName("Deve lançar erro de negocio ao tentar salavar um livro com isbn duplicado")
	public void shouldNotSaveWithDuplicateISBN() {
		//cenario
		Book book = createValidBook();
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
		
		Throwable execption =  Assertions.catchThrowable(()-> service.save(book));
		
		//verificações
		assertThat(execption).isInstanceOf(BusinessException.class).hasMessage("Isbn já cadastrada");
		//nunca vai chamar o metodo save
		Mockito.verify(repository,Mockito.never()).save(book);
	}
	
	
	
	private Book createValidBook() {
		return Book.builder().isbn("123").author("Marcelo").title("Meu Livro").build();
	}
	
	
}
