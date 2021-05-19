package italo.gerproc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import italo.gerproc.exception.UsuarioNaoEncontradoException;
import italo.gerproc.model.request.LoginRequest;
import italo.gerproc.model.response.ErroResponse;
import italo.gerproc.model.response.LoginResponse;
import italo.gerproc.service.UsuarioService;

@RestController
@RequestMapping( value="/api")
public class LoginController {

	@Autowired
	private UsuarioService usuarioService;
		
	@ResponseBody
	@PostMapping(value="/login")
	public ResponseEntity<Object> login( @RequestBody LoginRequest request ) {
		if ( request.getNomeUsuario() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_USUARIO_REQUERIDO ) );
		if ( request.getNomeUsuario().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NOME_USUARIO_REQUERIDO ) );
		
		if ( request.getSenha() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_SENHA_REQUERIDA ) );
		if ( request.getSenha().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_SENHA_REQUERIDA ) );
		
		try {
			LoginResponse resp = usuarioService.login( request );			
			return ResponseEntity.ok( resp );
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );			
		} 
	}
	
}
