package italo.gerproc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import italo.gerproc.exception.UsuarioJaExisteException;
import italo.gerproc.exception.UsuarioNaoEncontradoException;
import italo.gerproc.exception.UsuarioTipoInvalidoException;
import italo.gerproc.model.Usuario;
import italo.gerproc.model.UsuarioTipo;
import italo.gerproc.model.request.LoginRequest;
import italo.gerproc.model.request.SaveUsuarioRequest;
import italo.gerproc.model.response.LoginResponse;
import italo.gerproc.model.response.UsuarioResponse;
import italo.gerproc.repository.UsuarioRepository;
import italo.gerproc.util.HashUtil;
import italo.gerproc.util.JwtTokenUtil;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HashUtil hashUtil;
	
	@Autowired
	private JwtTokenUtil tokenUtil;
	
	public LoginResponse login( LoginRequest request ) throws UsuarioNaoEncontradoException {
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "nome_usuario", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase() )
				.withMatcher( "senha", ExampleMatcher.GenericPropertyMatchers.exact() );
		
		Usuario usuario = new Usuario();
		usuario.setNomeUsuario( request.getNomeUsuario() );
		usuario.setSenha( hashUtil.geraHash( request.getSenha() ) );
		
		Example<Usuario> ex = Example.of( usuario, em );
		
		Usuario u = usuarioRepository.findOne( ex ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		UsuarioResponse usuarioResp = new UsuarioResponse();
		usuarioResp.setId( u.getId() );
		usuarioResp.setNome( u.getNome() );
		usuarioResp.setNomeUsuario( u.getNomeUsuario() );
		usuarioResp.setTipo( u.getTipo() );
		
		String[] roles = {
			String.valueOf( u.getTipo() )
		};
				
		String token = tokenUtil.geraToken( request.getNomeUsuario(), roles );
		
		LoginResponse resp = new LoginResponse();
		resp.setUsuario( usuarioResp );
		resp.setToken( token );		
		return resp;
	}
	
	public void registra( SaveUsuarioRequest request ) throws UsuarioJaExisteException, UsuarioTipoInvalidoException {			
		if ( this.get( request.getNomeUsuario() ).isPresent() )
			throw new UsuarioJaExisteException();
		
		UsuarioTipo tipo = this.getTipo( request.getTipo() );
		if ( tipo == UsuarioTipo.INVALIDO )
			throw new UsuarioTipoInvalidoException();
		
		Usuario u = new Usuario();
		u.setNome( request.getNome() ); 
		u.setNomeUsuario( request.getNomeUsuario() );
		u.setSenha( hashUtil.geraHash( request.getSenha() ) );
		u.setTipo( tipo );
		
		usuarioRepository.save( u ); 
	}
	
	public void atualiza( Long uid, SaveUsuarioRequest request ) throws UsuarioNaoEncontradoException, UsuarioTipoInvalidoException, UsuarioJaExisteException {
		Usuario u = usuarioRepository.findById( uid ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		UsuarioTipo tipo = this.getTipo( request.getTipo() );
		if ( tipo == UsuarioTipo.INVALIDO )
			throw new UsuarioTipoInvalidoException();
		
		Optional<Usuario> uOp2 = this.get( request.getNomeUsuario() );
		if ( uOp2.isPresent() ) {
			Usuario u2 = uOp2.get();
			if ( u.getId() != u2.getId() )
				throw new UsuarioJaExisteException();			
		}
		
		u.setNome( request.getNome() );
		u.setNomeUsuario( request.getNomeUsuario() );
		u.setSenha( hashUtil.geraHash( request.getSenha() ) );
		u.setTipo( tipo );
		
		usuarioRepository.save( u );
	}
	
	public void deleta( Long uid ) throws UsuarioNaoEncontradoException {
		boolean existe = usuarioRepository.existsById( uid );
		if ( !existe )
			throw new UsuarioNaoEncontradoException();
		
		usuarioRepository.deleteById( uid ); 
	}
	
	public List<UsuarioResponse> buscaTodos() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		List<UsuarioResponse> lista = new ArrayList<>();
		for( Usuario u : usuarios ) {
			UsuarioResponse resp = new UsuarioResponse();
			this.carregaResponse( resp, u );
			
			lista.add( resp );
		}
		
		return lista;
	}
	
	public List<UsuarioResponse> filtra( UsuarioTipo tipo ) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		List<UsuarioResponse> lista = new ArrayList<>();
		for( Usuario u : usuarios ) {
			if ( u.getTipo() != tipo )
				continue;
			
			UsuarioResponse resp = new UsuarioResponse();
			this.carregaResponse( resp, u );
			
			lista.add( resp );
		}
		
		return lista;
	}
	
	public List<UsuarioResponse> filtraPorNomeIni( String nomeIni ) {
		Usuario usuario = new Usuario();
		usuario.setNome( nomeIni );
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "nome", ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase() );
		
		Example<Usuario> ex = Example.of( usuario, em );
		
		List<Usuario> usuarios = usuarioRepository.findAll( ex );
		
		List<UsuarioResponse> lista = new ArrayList<>();
		for( Usuario u : usuarios ) {
			UsuarioResponse resp = new UsuarioResponse();
			this.carregaResponse( resp, u );
			
			lista.add( resp );
		}
		
		return lista;
	}
	
	public UsuarioResponse get( Long uid ) throws UsuarioNaoEncontradoException {				
		Usuario u = usuarioRepository.findById( uid ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		UsuarioResponse resp = new UsuarioResponse();
		this.carregaResponse( resp, u );
			
		return resp;
	}
	
	public Optional<Usuario> get( String nomeUsuario ) {
		Usuario usuario = new Usuario();
		usuario.setNomeUsuario( nomeUsuario );
		
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher( "nomeUsuario", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase() );
		
		Example<Usuario> ex = Example.of( usuario, em );
		
		return usuarioRepository.findOne( ex );
	}
	
	private void carregaResponse( UsuarioResponse resp, Usuario u ) {
		resp.setId( u.getId() );
		resp.setNome( u.getNome() );
		resp.setNomeUsuario( u.getNomeUsuario() );
		resp.setTipo( u.getTipo() );
	}
	
	public UsuarioTipo getTipo( String tipo ) {
		if ( tipo.equalsIgnoreCase( "ADMIN" ) ) 
			return UsuarioTipo.ADMIN;
		if ( tipo.equalsIgnoreCase( "TRIADOR") )
			return UsuarioTipo.TRIADOR;
		if ( tipo.equalsIgnoreCase( "FINALIZADOR" ) )
			return UsuarioTipo.FINALIZADOR;
		return UsuarioTipo.INVALIDO;
	}
		
}
