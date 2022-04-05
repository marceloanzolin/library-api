package com.anzolinmarcelo.livariaapi.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.BindingResult;

public class ApiErros {
	
	private List<String> erros;
	
	public ApiErros(BindingResult bindingResult) {
		this.erros = new ArrayList<>();
		bindingResult.getAllErrors().forEach(error -> this.erros.add(error.getDefaultMessage()));
		
	}
	
	public ApiErros(BusinessException ex){
		this.erros = Arrays.asList(ex.getMessage());
	}
	
	public List<String> getErros(){
		return erros;
	}

}
