package com.sabado.livro.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sabado.livro.models.Livro;
import com.sabado.livro.repo.LivroRepository;

@Controller
@RequestMapping("/livro")
public class LivroController {

	@Autowired
	private LivroRepository livroRepo;
	private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/";
	
	
	@GetMapping("/")
	public String inicio(Model model) {
		model.addAttribute("livro", livroRepo.findAll());
		return  "index";
	}
	
	@GetMapping("/form")
	public String form(Model model) {
		model.addAttribute("livro", new Livro());
		return "form";
	}
	
	@GetMapping("/form/{id}")
	public String form (@PathVariable("id") Long id, Model model) {
		Optional<Livro> livro = livroRepo.findById(id);
		if(livro.isPresent()) {
			model.addAttribute("livro", livro.get());
		}else {
			model.addAttribute("livro", new Livro());
		}
		return "form";
	}

	
	@PostMapping("/add")
	public String addLivro(@RequestParam("id") Optional<Long> id,
			@RequestParam("nome") String nome,
			@RequestParam("qtdPaginas") int qtdPaginas,
			@RequestParam("autor") String autor,
			@RequestParam("imagem") MultipartFile imagem) {
		
		Livro livro;
		if(id.isPresent()) {
			livro = livroRepo.findById(id.get()).orElse(new Livro());
			
		}else {
			livro = new Livro();
			
		}
		
		livro.setNome(nome);
		livro.setQtdPaginas(qtdPaginas);
		livro.setAutor(autor);
		
		livroRepo.save(livro); // Salvar dentro do banco de dados
		
		// Salvar a imagem
		if(!imagem.isEmpty()) {
			try {
				//Lógica para salvar a imagem
				// filme_32_imagemthor.png
				String fileName = "livro_" + livro.getId() + "_" + imagem.getOriginalFilename();
				
				Path path = Paths.get(UPLOAD_DIR + fileName); // Capturando caminho completo
				Files.write(path, imagem.getBytes()); // Escrevendo a imagem
				livro.setImagem("/" + fileName); //Adicionar o caminho para acessar a imagem
				
				livroRepo.save(livro); // Salvar a imagem
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/livro/";
	}
	
	@GetMapping("/delete/{id}")

	public String deleteLivro(@PathVariable("id") Long id) {
	Optional<Livro> livro = livroRepo.findById(id);

	if(livro.isPresent()) {
	Livro livroParaDeletar = livro.get();
	String imagePath = UPLOAD_DIR + livroParaDeletar.getImagem();
	try {
	Files.deleteIfExists(Paths.get(imagePath));
	} catch (Exception e) {

	}

	livroRepo.deleteById(id);

	}

	return "redirect:/livro/";

	}
	
}
