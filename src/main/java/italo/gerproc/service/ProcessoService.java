package italo.gerproc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.gerproc.exception.NaoCriadorException;
import italo.gerproc.exception.ParecerNaoEncontradoException;
import italo.gerproc.exception.ProcessoNaoEncontradoException;
import italo.gerproc.exception.UsuarioNaoEncontradoException;
import italo.gerproc.model.Parecer;
import italo.gerproc.model.Processo;
import italo.gerproc.model.Usuario;
import italo.gerproc.model.request.SaveParecerRequest;
import italo.gerproc.model.request.SaveProcessoRequest;
import italo.gerproc.model.response.ParecerResponse;
import italo.gerproc.model.response.ProcessoResponse;
import italo.gerproc.model.response.UsuarioResponse;
import italo.gerproc.repository.ParecerRepository;
import italo.gerproc.repository.ProcessoRepository;
import italo.gerproc.repository.UsuarioRepository;
import italo.gerproc.util.DataUtil;

@Service
public class ProcessoService {

	@Autowired
	private UsuarioRepository usuarioRepository;
			
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private ParecerRepository parecerRepository;
	
	@Autowired
	private DataUtil dataUtil;
	
	public void registra( SaveProcessoRequest request ) throws UsuarioNaoEncontradoException {
		Processo p = new Processo();
		p.setDescricao( request.getDescricao() );
		p.setUltimaAlteracao( new Date() );
				
		Usuario t = new Usuario();
		t.setId( request.getTriadorId() ); 
		p.setUsuario( t ); 

		p = processoRepository.save( p );
				
		for( Long finalizadorId : request.getFinalizadoresIds() ) {
			Usuario f = new Usuario();
			f.setId( finalizadorId ); 
			
			Parecer par = new Parecer();
			par.setMensagem( "" ); 
			par.setFinalizado( false );
			par.setUltimaAlteracao( new Date() );
			par.setProcesso( p ); 
			par.setUsuario( f );
			
			parecerRepository.save( par );
		}				
	}
	
	public void registraParecerMensagem( Long parecerId, SaveParecerRequest request ) throws ParecerNaoEncontradoException {						
		Parecer par = parecerRepository.findById( parecerId ).orElseThrow( ParecerNaoEncontradoException::new );
		par.setMensagem( request.getMensagem() ); 
		par.setUltimaAlteracao( new Date() ); 
		par.setFinalizado( true );
		
		parecerRepository.save( par );
	}
		
	public void deletaProcesso( String nomeUsuario, Long processoId ) throws UsuarioNaoEncontradoException, ProcessoNaoEncontradoException, NaoCriadorException {
		Usuario u = usuarioRepository.findByNomeUsuario( nomeUsuario ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		if ( !u.getNomeUsuario().equalsIgnoreCase( nomeUsuario ) )
			throw new NaoCriadorException();
			
		boolean existe = processoRepository.existsById( processoId );
		if ( !existe )
			throw new ProcessoNaoEncontradoException();
		
		processoRepository.deleteById( processoId ); 
	}
	
	public void deletaParecer( Long parecerId ) throws ParecerNaoEncontradoException {
		boolean existe = parecerRepository.existsById( parecerId );
		if ( !existe )
			throw new ParecerNaoEncontradoException();
		
		parecerRepository.deleteById( parecerId ); 
	}
	
	public List<ProcessoResponse> buscaProcessosPorTriador( Long uid ) throws UsuarioNaoEncontradoException {
		Usuario u = usuarioRepository.findById( uid ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		List<Processo> processos = u.getProcessos();
		List<ProcessoResponse> lista = new ArrayList<>();
		for( Processo p : processos ) {
			ProcessoResponse resp = new ProcessoResponse();
			this.carregaResponse( resp, p ); 
			lista.add( resp );
		}
		return lista;
	}
	
	public List<ParecerResponse> buscaPareceresPorFinalizador( Long finalizadorId ) throws UsuarioNaoEncontradoException {
		Usuario u = usuarioRepository.findById( finalizadorId ).orElseThrow( UsuarioNaoEncontradoException::new );
		
		List<Parecer> pareceres = u.getPareceres();
		List<ParecerResponse> lista = new ArrayList<>();
		for( Parecer p : pareceres ) {
			ParecerResponse resp = new ParecerResponse();
			this.carregaParecerResponse( resp, p );
			lista.add( resp );
		}
		return lista;
	}
			
	public ProcessoResponse buscaProcessoPorId( Long processoId ) throws ProcessoNaoEncontradoException {
		Processo p = processoRepository.findById( processoId ).orElseThrow( ProcessoNaoEncontradoException::new );
		
		ProcessoResponse resp = new ProcessoResponse();
		this.carregaResponse( resp, p );
		
		return resp;
	}
	
	public ParecerResponse buscaParecerPorId( Long parecerId ) throws ParecerNaoEncontradoException {
		Parecer p = parecerRepository.findById( parecerId ).orElseThrow( ParecerNaoEncontradoException::new );
		
		ParecerResponse resp = new ParecerResponse();
		this.carregaParecerResponse( resp, p );
		
		return resp;
	}
		
	private void carregaResponse( ProcessoResponse resp, Processo p ) {
		resp.setId( p.getId() ); 
		resp.setUltimaAlteracao( dataUtil.dataParaString( p.getUltimaAlteracao() ) );
		resp.setDescricao( p.getDescricao() );
		
		Usuario t = p.getUsuario();
		
		UsuarioResponse triadorResp = new UsuarioResponse();		
		this.carregaUsuarioResponse( triadorResp, t );
				
		List<ParecerResponse> pareceresResp = new ArrayList<>();
		for( Parecer par : p.getPareceres() ) {
			ParecerResponse parResp = new ParecerResponse();			
			this.carregaParecerResponse( parResp, par );
			
			pareceresResp.add( parResp );
		}
		
		resp.setTriador( triadorResp );
		resp.setPareceres( pareceresResp );		
	}
	
	private void carregaUsuarioResponse( UsuarioResponse resp, Usuario u ) {
		resp.setId( u.getId() );
		resp.setNome( u.getNome() );
		resp.setNomeUsuario( u.getNomeUsuario() );
		resp.setTipo( u.getTipo() ); 
	}
	
	private void carregaParecerResponse( ParecerResponse resp, Parecer p ) {
		resp.setId( p.getId() );
		resp.setMensagem( p.getMensagem() );
		resp.setFinalizado( p.isFinalizado() );
		
		resp.setUltimaAlteracao( dataUtil.dataParaString( p.getUltimaAlteracao() ) );
				
		UsuarioResponse finalizadorResp = new UsuarioResponse();		
		this.carregaUsuarioResponse( finalizadorResp, p.getUsuario() );
		resp.setFinalizador( finalizadorResp );		
		
		Processo proc = p.getProcesso();		
		ProcessoResponse procResp = new ProcessoResponse();		
		procResp.setId( proc.getId() ); 
		procResp.setUltimaAlteracao( dataUtil.dataParaString( proc.getUltimaAlteracao() ) );
		procResp.setDescricao( proc.getDescricao() );
		procResp.setPareceres( new ArrayList<>(0) ); 
		resp.setProcesso( procResp );		

		Usuario t = p.getUsuario();		
		UsuarioResponse triadorResp = new UsuarioResponse();		
		this.carregaUsuarioResponse( triadorResp, t );
		procResp.setTriador( triadorResp ); 
		
	}
	
}
