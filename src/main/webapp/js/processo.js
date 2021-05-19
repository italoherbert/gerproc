
class Processo extends React.Component {
	
	constructor(props) {
		super(props);
		this.state = { 
			status : 0, 
			backendErroCodigo : 0, 
			processo : null,
			sucessoMsg : null,
			erroMsg : null
		};		
	}
		
	componentDidMount() {					
		fetch( "/api/processo/get/"+this.props.processoId, {
			method : "GET",			
			credentials : "include",
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}							 
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				
				if ( resposta.status != 200 )					
					this.state.codigo = dados.codigo;	
				
				this.state.status = resposta.status;
				this.state.processo = dados;
				this.state.sucessoMsg = "Processo carregado com êxito!";
				
				this.setState( this.state );
			} );			 
		} );		
	}
		
	limpaMsgs() {		
		this.state.status = 0;
		this.state.backendErroCodigo = 0;	
		this.state.sucessoMsg = null;
		this.state.erroMsg = null;
		
		this.setState( this.state );
	}
	
	paraAlterarParecer( parecerId ) {
		ReactDOM.render( <Parecer parecerId={parecerId} />, document.getElementById("root") );
	}
		
	render() {
		const { status, backendErroCodigo, erroMsg, sucessoMsg, processo } = this.state;
		
		if ( processo == null )
			return ( <span></span> );
		
		return (
			<div className="container">
				<MensagemPainel tipo="backend-erro" status={status} codigo={backendErroCodigo} />				
				<MensagemPainel tipo="frontend-erro" msg={erroMsg} />				
				<MensagemPainel tipo="sucesso" msg={sucessoMsg} />
				
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6 bg-white">
						<h4 className="text-center">Tela de processo</h4>
						<div class="form-group">
							<h6>Descrição:</h6>
							<p>{processo.descricao}</p>
						</div>
					</div>
				</div>
			
				<br />
				{processo.pareceres.map( ( parecer, index ) => {
					return (
						<div>
							<div className="row">
								<div className="col-md-3"></div>
								<div className="card col-md-6 bg-light">
									<div className="card-body">
										<h6 className="card-title">
											Parecer de: 
											<span className="text-primary">{parecer.finalizador.nome}</span>
										</h6>
										<h6 className="card-title">
											Registrado em: 
											<span className="text-primary">{parecer.ultimaAlteracao}</span>
										</h6>
										<p>{parecer.mensagem}</p>										
										<div className="form-group">
											{parecer.finalizado ? "" : "Parecer pendente!"}
										</div>
										<div className="form-group text-right">
											<button className="btn btn-primary" onClick={() => this.paraAlterarParecer( parecer.id ) }>Alterar</button>												
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