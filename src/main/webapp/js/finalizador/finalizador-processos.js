	
class FinalizadorProcessos extends React.Component {	
	
	constructor(props) {
		super(props);
		this.state = { 
			status : 0, 
			backendErroCodigo : 0, 
			pareceres : [],
			sucessoMsg : null,
			erroMsg : null
		};
		this.filtrar = this.filtrar.bind( this );
		this.somentePendentesOnChange = this.somentePendentesOnChange.bind( this );
	}
	
	componentDidMount() {
		this.filtrar( null );
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
						
		this.limpaMsgs();				
											
		fetch( "/api/parecer/lista/"+this.props.finalizadorId, {
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
				this.state.pareceres = dados;
				
				if ( dados.length == 0 ) {
					this.state.sucessoMsg = "Você não tem nenhum processo para registrar parecer!";
				} else {					
					this.state.sucessoMsg = "Processos listados com êxito!";
				}
				
				this.setState( this.state );
			} );			 
		} );		
	}
			
	alterar( e, parecerId ) {						
		ReactDOM.render( <Parecer parecerId={parecerId} />, document.getElementById( "root" ) );
	}
		
	somentePendentesOnChange( e ) {				
		this.setState( this.state );
	}		
			
	limpaMsgs() {		
		this.state.status = 0;
		this.state.backendErroCodigo = 0;	
		this.state.sucessoMsg = null;
		this.state.erroMsg = null;
		
		this.setState( this.state );
	}	
			
	render() {
		const { status, backendErroCodigo, erroMsg, sucessoMsg, somentePendentes, pareceres } = this.state;
		return (
			<div className="container">
				<MensagemPainel tipo="backend-erro" status={status} codigo={backendErroCodigo} />				
				<MensagemPainel tipo="frontend-erro" msg={erroMsg} />				
				<MensagemPainel tipo="sucesso" msg={sucessoMsg} />
				
				<div className="row">
					<h4 className="text-center col-md-12">Seus processos atribuidos</h4>
				</div>
				
				<div className="row">
					<div className="col-md-12 text-center">
						<form onSubmit={this.somentePendentesOnChange}>
							<div class="form-group">
								<input type="checkbox" ref="somentePendentes" onChange={this.somentePendentesOnChange} value="Somente pendentes" defaultChecked="checked" />
								Somente pendentes
							</div>
						</form>
					</div>				
				</div>
				
				{pareceres.map( ( parecer, index ) => {
					if ( this.refs.somentePendentes.checked == true )
						if ( parecer.finalizado == true )
							return (<span></span>);
					
					return (
						<div>
							<div className="row">
								<div className="col-md-3"></div>
								<div className="card bg-light col-md-6">
									<div className="card-body text-left">
										<h6 className="text-left card-title">
											Processo criado por: 
											<span className="text-primary">{parecer.processo.triador.nome}</span>
										</h6>
										<h6 className="text-left card-title">
											Registrado em: 
											<span className="text-primary">{parecer.processo.ultimaAlteracao}</span>
										</h6>
										<p>{parecer.processo.descricao}</p>
										
										<p className="text-primary">{parecer.finalizado == true ? "" : "Pendente!"}</p>
										
										<div className="form-group text-right">
											<button className="btn btn-primary" onClick={(e) => this.alterar( e, parecer.id ) }>Alterar</button>												
										</div>
									</div>
								</div>
							</div>
							<br />						
						</div>
					);	
				} ) }
			</div>
		);
	}
}