	
class TriadorProcessos extends React.Component {	
	
	constructor(props) {
		super(props);
		this.state = { 
			status : 0, 
			backendErroCodigo : 0, 
			processos : [],
			finalizadores : [],
			finalizadoresSelecionados : [],
			sucessoMsg : null,
			erroMsg : null
		};
		this.filtrar = this.filtrar.bind( this );
		this.registrar = this.registrar.bind( this );
	}
	
	componentDidMount() {
		this.filtrar( null );
		this.carregaListaFinalizadores();
		window.scrollTo( 0, 0 );
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();	
			
		this.limpaMsgs();
									
		fetch( "/api/processo/lista/"+this.props.triadorId, {
			method : "GET",			
			credentials : "include",
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}							 
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				
				let codigo = 0;
				if ( resposta.status != 200 )					
					codigo = dados.codigo;	
				
				this.state.status = resposta.status;
				this.state.codigo = codigo;
				this.state.processos = dados;
				if ( dados.length == 0 ) {
					this.state.sucessoMsg = "Você não tem nenhum processo registrado até agora!";
				} else {					
					this.state.sucessoMsg = "Processos listados com êxito!";
				}
				
				this.setState( this.state );
			} );			 
		} );		
	}
	
	carregaListaFinalizadores() {										
		fetch( "/api/usuario/lista/finalizadores", {
			method : "GET",			
			credentials : "include",
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}							 
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				
				let codigo = 0;
				if ( resposta.status != 200 )					
					codigo = dados.codigo;	
				
				this.state.status = resposta.status;
				this.state.codigo = codigo;
				this.state.finalizadores = dados;

				if ( dados.length == 0 )
					this.state.erroMsg = "Não há nenhum usuário com perfil finalizador registrado!";				
				
				this.setState( this.state );
			} );			 
		} );
	}
	
	visualizar( e, processoId ) {		
		ReactDOM.render( <Processo processoId={processoId} />, document.getElementById( "root" ) );
	}
				
	remover( e, processoId ) {
		this.limpaMsgs();
		
		fetch( "/api/processo/deleta/"+processoId, {
			method : "DELETE",			
			credentials : "include",
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}							 
		} ).then( (resposta) => {	
			this.state.status = resposta.status;

			if ( resposta.status == 200 ) {
				this.state.sucessoMsg = "Processo removido com êxito!";				
			} else {				
				resposta.json().then( (dados) => {
					if ( resposta.status == 400 )
						this.state.backendErroCodigo = dados.codigo; 
					
					this.setState( this.state );
				} );
			}
						
			this.setState( this.state );
			
			this.filtrar( e );
		} );
	}
	
	registrar( e ) {		
		e.preventDefault();
		
		this.limpaMsgs();
		
		let finalizadoresIds = new Array();
		for( let i = 0; i < this.state.finalizadoresSelecionados.length; i++ )
			finalizadoresIds.push( this.state.finalizadoresSelecionados[ i ].id );									
						
		fetch( "/api/processo/registra", {
			method : "POST",
			credentials : "include",
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer " + sistema.token 
			},				
			body : JSON.stringify( { 
				descricao : this.refs.descricao.value,
				triadorId : this.props.triadorId,
				finalizadoresIds : finalizadoresIds
			} )
		} ).then( (resposta) => {
			this.state.status = resposta.status;
			if ( resposta.ok ) {
				this.state.sucessoMsg = "Processo registrado com êxito!";											
				this.setState( this.state );
				
				this.limpaForm();
				this.filtrar( e );				
				window.scrollTo( 0, 0 );					
			} else {
				resposta.json().then( (dados) => {
					if ( resposta.status == 400 )
						this.state.backendErroCodigo = dados.codigo; 
					
					this.setState( this.state );
					window.scrollTo( 0, 0 );					
				} );
			}
		} );	
	}	
	
	limpaForm() {
		this.refs.descricao.value = "";
		this.state.finalizadoresSelecionados = [];
		this.setState( this.state );
		
		this.carregaListaFinalizadores();
	}
	
	limpaMsgs() {		
		this.state.status = 0;
		this.state.backendErroCodigo = 0;	
		this.state.sucessoMsg = null;
		this.state.erroMsg = null;
		
		this.setState( this.state );
	}
	
	addFinalizador( e ) {
		e.preventDefault();
		
		let list = this.refs.finalizadores.options;
		let selecionada = null;
		for( let i = 0; selecionada == null && i < list.length; i++ )
			if ( list[ i ].selected == true )
				selecionada = list[ i ];
						
		let finalizadores = this.state.finalizadores;
		let finalizadoresSelecionados = this.state.finalizadoresSelecionados;				
						
		let achou = false;
		for( let i = 0; !achou && i < finalizadores.length; i++ ) {
			if ( finalizadores[ i ].id == selecionada.value ) { 
				finalizadoresSelecionados.push( finalizadores[ i ] );
				finalizadores.splice( i, 1 );
				
				achou = true;
			}
		}		

		this.setState( this.state );
	}
	
	removeFinalizador( e, finalizadorId ) {
		e.preventDefault();
				
		let finalizadores = this.state.finalizadores;
		let finalizadoresSelecionados = this.state.finalizadoresSelecionados;
				
		let achou = false;
		for( let i = 0; !achou && i < finalizadoresSelecionados.length; i++ ) {
			if ( finalizadoresSelecionados[ i ].id == finalizadorId ) { 
				finalizadores.push( finalizadoresSelecionados[ i ] );
				finalizadoresSelecionados.splice( i, 1 );
				
				achou = true;
			}
		}		
		
		this.setState( this.state );
	}
		
	render() {
		const { status, backendErroCodigo, erroMsg, sucessoMsg, processos, finalizadores, finalizadoresSelecionados } = this.state;
		return (
			<div className="container">
				<MensagemPainel tipo="backend-erro" status={status} codigo={backendErroCodigo} />				
				<MensagemPainel tipo="frontend-erro" msg={erroMsg} />				
				<MensagemPainel tipo="sucesso" msg={sucessoMsg} />
				
				<div className="row">
					<h4 className="text-center col-md-12">Seus processos criados</h4>
				</div>
				
				{processos.map( ( processo, index ) => {
					return (
						<div>
							<div className="row">
								<div className="col-md-3"></div>
								<div className="card bg-light col-md-6">
									<div className="card-body text-left">
										<h6 className="text-left card-title">
											Processo criado por: 
											<span className="text-primary">{processo.triador.nome}</span>
										</h6>
										<h6 className="text-left card-title">
											Registrado em: 
											<span className="text-primary">{processo.ultimaAlteracao}</span>
										</h6>
										<p>{processo.descricao}</p>
										<div className="form-group text-right">
											<button className="btn btn-primary" onClick={(e) => this.visualizar( e, processo.id ) }>Visualizar</button>												
											<button className="btn btn-primary ml-1" onClick={(e) => this.remover( e, processo.id ) }>Remover</button>												
										</div>
									</div>
								</div>
							</div>
							<br />						
						</div>
					);	
				} ) }
				
				<div class="row">
					<div className="col-md-3"></div>
					<div className="card col-md-6">
						<div className="card-body">
							<h4 className="card-title">Registre um processo</h4>
							
							<form onSubmit={this.registrar}>
								<div className="form-group">
									<label className="control-label" for="descricao">Descrição:</label>
									<br />
									<textarea ref="descricao" name="descricao" className="form-control" />						
								</div>																		
								
								<fieldset className="card">
									<div className="card-body">
										<legend className="card=title">Finalizadores</legend>
										<div className="form-group">
											<select ref="finalizadores" name="finalizadores" className="d-inline-block">
												{finalizadores.map( (usuario) => {
													return (
														<option value={usuario.id}>{usuario.nome}</option>
													);
												})}
											</select>
											
											<button className="btn btn-primary ml-1" onClick={(e) => this.addFinalizador( e ) }>
												+
											</button>												
										</div>
										
										<legend className="card=title">Finalizadores selecionados</legend>
										<table className="table">
											{finalizadoresSelecionados.map( (usuario) => {
												return (
													<tr>
														<td>{usuario.nome}</td>
														<td>
															<button className="btn btn-link m-0 p-0" onClick={ (e) => this.removeFinalizador( e, usuario.id ) }>
																remover
															</button>
														</td>
													</tr>												
												);
											} )}
										</table>
									</div>
								</fieldset>																	
								
								<br />
								<div className="form-group">
									<input type="submit" value="Registrar" className="btn btn-primary col-sm-offset-2" />				
								</div>				
							</form>								
						</div>
					</div>
				</div>										 
			</div>
		);
	}
	
}