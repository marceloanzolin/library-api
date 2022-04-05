package com.anzolinmarcelo.livariaapi.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.anzolinmarcelo.livariaapi.dto.BookDTO;
import com.anzolinmarcelo.livariaapi.exception.BusinessException;
import com.anzolinmarcelo.livariaapi.model.entity.Book;
import com.anzolinmarcelo.livariaapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {
	
	static String BOOK_API = "/api/books";
	@Autowired
	MockMvc mvc; //simula uma requisicao
	
	@MockBean
	BookService service;
	
	@Test
	@DisplayName("Deve criar um livro com Sucesso")
	public void createBookTest() throws Exception {
		
		//cria o json
		BookDTO dto = createNewBook();
		
		Book saveBook = Book.builder().id(10l).author("Marcelo").title("AS Aventuras").isbn("001").build();
		
		//salva para simular o retorno Retornou o livro salvo
		BDDMockito.given(service.save(Mockito.any(Book.class)))
		.willReturn(saveBook);
		
		//transforma para json
		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(BOOK_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(json);
		
		//verifca se o objeto esta criado e se o id não esta vazio  // acertivas andExpect
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("id").value(10l))
		.andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
		.andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
		.andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()));
		
	}

	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação dos livros")
	public void createInvalidTest() throws Exception {
		
		//transforma para json
				String json = new ObjectMapper().writeValueAsString(new BookDTO());
				
				MockHttpServletRequestBuilder request = MockMvcRequestBuilders
						.post(BOOK_API)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json);
				
				mvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("erros", Matchers.hasSize(3)));
				
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com ISBN já utilizado")
	public void createBookWithDuplicateIsbn()  throws Exception{
		
		BookDTO dto = createNewBook();
		
		String json = new ObjectMapper().writeValueAsString(dto);
		String mensagemErro = "Isbn já cadastrada";
		BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException(mensagemErro));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("erros",Matchers.hasSize(1)))
		.andExpect(jsonPath("erros[0]").value(mensagemErro));
		
	}
	
	
	private BookDTO createNewBook() {
		return BookDTO.builder().author("Marcelo").title("AS Aventuras").isbn("001").build();
	}
	
	
	

}
