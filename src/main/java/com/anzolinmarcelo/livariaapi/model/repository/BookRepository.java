package com.anzolinmarcelo.livariaapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anzolinmarcelo.livariaapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	boolean existsByIsbn(String isbn);

}
