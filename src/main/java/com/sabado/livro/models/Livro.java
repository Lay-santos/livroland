package com.sabado.livro.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor

public class Livro {

	@Id
	@GeneratedValue
	private Long id;

	private String nome;
	private String autor;
	private String imagem;
	private int qtdPaginas;

}