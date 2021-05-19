package italo.gerproc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import italo.gerproc.exception.NaoCriadorException;
import italo.gerproc.exception.ParecerNaoEncontradoException;
import italo.gerproc.exception.ProcessoNaoEncontradoException;
import italo.gerproc.exception.UsuarioNaoEncontradoException;
import italo.gerproc.model.request.SaveParecerRequest;
import italo.gerproc.model.request.SaveProcessoRequest;
import italo.gerproc.model.response.ErroResponse;
import italo.gerproc.model.response.ParecerResponse;
import italo.gerproc.model.response.ProcessoResponse;
import italo.gerproc.service.ProcessoService;
import italo.gerproc.util.JwtTokenUtil;

@RestController
@RequestMapping(value="/api")
public class ProcessoController {

	@Autowired
	private ProcessoService processoService;
	
	@Autowired
	private JwtTokenUtil tokenUtil;

	@ResponseBody
	@PreAuthorize("hasAuthority('TRIADOR')")
	@PostMapping(value="/processo/registra")
	public ResponseEntity<Object> registraProcesso( @RequestBody SaveProcessoRequest request ) {
		if ( request.getDescricao() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PROCESSO_DESCRICAO_REQUERIDA ) );
		if ( request.getDescricao().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PROCESSO_DESCRICAO_REQUERIDA ) );
		
		if ( request.getFinalizadoresIds() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PROCESSO_FINALIZADOR_REQUERIDO ) );
		if ( request.getFinalizadoresIds().length == 0 )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PROCESSO_FINALIZADOR_REQUERIDO ) );		
		
		try {
			processoService.registra( request );
			return ResponseEntity.ok().build();
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );						
		}
	}

	@ResponseBody
	@PreAuthorize("hasAuthority('FINALIZADOR')")
	@PatchMapping(value="/parecer/altera/{parecerId}")
	public ResponseEntity<Object> registraParecer( @PathVariable Long parecerId, @RequestBody SaveParecerRequest request ) {
		if ( request.getMensagem() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARECER_MENSAGEM_REQUERIDA ) );
		if ( request.getMensagem().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARECER_MENSAGEM_REQUERIDA ) );
		
		
		try {
			processoService.registraParecerMensagem( parecerId, request );
			return ResponseEntity.ok().build();
		} catch (ParecerNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARECER_NAO_ENCONTRADO ) );						
		}
	}	

	@PreAuthorize("hasAnyAuthority('TRIADOR', 'FINALIZADOR')")
	@GetMapping(value="/processo/get/{processoId}")
	public ResponseEntity<Object> getProcesso( @PathVariable Long processoId ) {
		try {
			ProcessoResponse processo = processoService.buscaProcessoPorId( processoId );
			return ResponseEntity.ok( processo );
		} catch (ProcessoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PROCESSO_NAO_ENCONTRADO ) );						
		}
	}
	
	@PreAuthorize("hasAnyAuthority('TRIADOR', 'FINALIZADOR')")
	@GetMapping(value="/parecer/get/{parecerId}")
	public ResponseEntity<Object> getParecer( @PathVariable Long parecerId ) {
		try {
			ParecerResponse parecer = processoService.buscaParecerPorId( parecerId );
			return ResponseEntity.ok( parecer );
		} catch (ParecerNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARECER_NAO_ENCONTRADO ) );						
		}
	}
	
	@PreAuthorize("hasAnyAuthority('TRIADOR', 'FINALIZADOR')")
	@GetMapping(value="/processo/lista/{triadorId}")
	public ResponseEntity<Object> buscaProcessos( @PathVariable Long triadorId ) {
		try {
			List<ProcessoResponse> pareceres = processoService.buscaProcessosPorTriador( triadorId );
			return ResponseEntity.ok( pareceres );
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );						
		}
	}
	
	@PreAuthorize("hasAnyAuthority('TRIADOR', 'FINALIZADOR')")
	@GetMapping(value="/parecer/lista/{finalizadorId}")
	public ResponseEntity<Object> buscaPareceres( @PathVariable Long finalizadorId ) {
		try {
			List<ParecerResponse> pareceres = processoService.buscaPareceresPorFinalizador( finalizadorId );
			return ResponseEntity.ok( pareceres );
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );						
		}
	}

	@PreAuthorize("hasAuthority('TRIADOR')")
	@DeleteMapping(value="/processo/deleta/{processoId}")
	public ResponseEntity<Object> deletaProcesso( 
			@RequestHeader("Authorization") String authToken, @PathVariable Long processoId ) {
		String token = tokenUtil.extraiBearerToken( authToken );
		String nomeUsuario = tokenUtil.getSubject( token );
		
		try {
			processoService.deletaProcesso( nomeUsuario, processoId );
			return ResponseEntity.ok().build();
		} catch (ProcessoNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PROCESSO_NAO_ENCONTRADO ) );						
		} catch (UsuarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_NAO_ENCONTRADO ) );									
		} catch (NaoCriadorException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.SER_CRIADOR_REQUERIDO ) );												
		}
	}
	
	@PreAuthorize("hasAuthority('TRIADOR')")
	@DeleteMapping(value="/parecer/deleta/{parecerId}")
	public ResponseEntity<Object> deletaParecer( @PathVariable Long parecerId ) {
		try {
			processoService.deletaParecer( parecerId );
			return ResponseEntity.ok().build();
		} catch (ParecerNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PARECER_NAO_ENCONTRADO ) );						
		}
	}
	
}
