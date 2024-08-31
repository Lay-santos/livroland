package com.sabado.livro.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sabado.livro.models.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long>{
		
	}

