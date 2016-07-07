package com.algaworks.socialbooks.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.socialbooks.domain.Comentario;
import com.algaworks.socialbooks.domain.Livro;
import com.algaworks.socialbooks.repository.ComentariosRepository;
import com.algaworks.socialbooks.repository.LivrosRepository;
import com.algaworks.socialbooks.services.exceptions.LivroNaoEncontradoException;

@Service
public class LivrosServices {

	@Autowired
	private LivrosRepository livrosRepository;
	
	@Autowired
	private ComentariosRepository comentariosRepository;
	
	public List<Livro> listar(){
		
		return this.livrosRepository.findAll();
	}
	
	public Livro buscar(Long id){
		
		Livro livro = this.livrosRepository.findOne(id);
		
		if(livro == null)
			throw new LivroNaoEncontradoException("O livro não pode ser encontrado.");
		
		return livro;
	}
	
	public Livro salvar(Livro livro){
		
		livro.setId(null);
		return this.livrosRepository.save(livro);
	}
	
	public void deletar(Long id){
		
		try{
			
			this.livrosRepository.delete(id);
		}
		
		catch(EmptyResultDataAccessException exception){
		
			throw new LivroNaoEncontradoException("O livro não pode ser encontrado.");
		}
	}
	
	public void atualizar(Livro livro){
		
		this.verificaExistencia(livro);
		this.livrosRepository.save(livro);
	}
	
	private void verificaExistencia(Livro livro){
		
		this.buscar(livro.getId());
	}
	
	public Comentario salvarComentario(Long livroId, Comentario comentario){
		
		Livro livro = this.buscar(livroId);
		
		comentario.setLivro(livro);
		comentario.setData(new Date());
		
		return this.comentariosRepository.save(comentario);
	}
	
	public List<Comentario> listarComentarios(Long livroId){
		
		Livro livro = this.buscar(livroId);
		
		return livro.getComentarios();
	}
}
