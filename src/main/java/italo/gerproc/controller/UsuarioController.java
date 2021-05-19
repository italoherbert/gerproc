package italo.gerproc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import italo.gerproc.exception.UsuarioJaExisteException;
import italo.gerproc.exception.UsuarioNaoEncontradoException;
import italo.gerproc.exception.UsuarioTipoInvalidoException;
import italo.gerproc.model.UsuarioTipo;
import italo.gerproc.model.request.SaveUsuarioRequest;
import italo.gerproc.model.response.ErroResponse;
import italo.gerproc.model.response.UsuarioResponse;
import italo.gerproc.service.UsuarioService;

@RestController
@RequestMapping(value="/api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@ResponseBody
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(value="/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveUsuarioRequest request ) {
		if ( request.getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_REQUERIDO ) );
		if ( request.getNome().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_REQUERIDO ) );
		
		if ( request.getNomeUsuario() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_USUARIO_REQUERIDO ) );
		if ( request.getNomeUsuario().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_USUARIO_REQUERIDO ) );
		
		if ( request.getSenha() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_SENHA_REQUERIDA ) );
		if ( request.getSenha().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_SENHA_REQUERIDA ) );		

		if ( request.getTipo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_REQUERIDO ) );
		if ( request.getTipo().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_REQUERIDO ) );
		
		try {
			usuarioService.registra( request );
			return ResponseEntity.ok().build();
		} catch ( UsuarioJaExisteException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );
		} catch ( UsuarioTipoInvalidoException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_DESCONHECIDO ) );
		}
		
	}
	
	@ResponseBody
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping(value="/atualiza/{uid}")
	public ResponseEntity<Object> registra( @PathVariable Long uid, @RequestBody SaveUsuarioRequest request ) {
		if ( request.getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_REQUERIDO ) );
		if ( request.getNome().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_REQUERIDO ) );
		
		if ( request.getNomeUsuario() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_USUARIO_REQUERIDO ) );
		if ( request.getNomeUsuario().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_USUARIO_REQUERIDO ) );
		
		if ( request.getSenha() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_SENHA_REQUERIDA ) );
		if ( request.getSenha().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_SENHA_REQUERIDA ) );		

		if ( request.getTipo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_REQUERIDO ) );
		if ( request.getTipo().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_REQUERIDO ) );
		
		try {
			usuarioService.atualiza( uid, request );
			return ResponseEntity.ok().build();
		} catch ( UsuarioNaoEncontradoException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );
		} catch ( UsuarioJaExisteException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );
		} catch ( UsuarioTipoInvalidoException e ) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_DESCONHECIDO ) );
		}		
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping(value="/deleta/{uid}")
	public ResponseEntity<Object> deleta( @PathVariable Long uid ) {
		try {
			usuarioService.deleta( uid );
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );			
		}
	}	

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value="/get/{uid}")
	public ResponseEntity<Object> get( @PathVariable Long uid ) {
		try {
			UsuarioResponse resp = usuarioService.get( uid );
			return ResponseEntity.ok( resp );
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );			
		}
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@ResponseBody
	@GetMapping(value="/filtra/{nomeIni}")
	public ResponseEntity<Object> filtra( @PathVariable String nomeIni ) {
		List<UsuarioResponse> lista;
		if ( nomeIni.trim().equals( "*" ) ) {
			lista = usuarioService.buscaTodos();
		} else {
			lista = usuarioService.filtraPorNomeIni( nomeIni );
		}
		return ResponseEntity.ok( lista );		
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'TRIADOR')")
	@ResponseBody
	@GetMapping(value="/lista/finalizadores")
	public ResponseEntity<Object> listaFinalizadores() {
		List<UsuarioResponse> lista = usuarioService.filtra( UsuarioTipo.FINALIZADOR );		
		return ResponseEntity.ok( lista );		
	}
	
}
