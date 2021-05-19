
class Parecer extends React.Component {
	
	constructor(props) {
		super(props);
		this.state = { 
			status : 0, 
			backendErroCodigo : 0, 
			parecer : null,
			sucessoMsg : null,
			erroMsg : null
		};
		this.alterarMensagem = this.alterarMensagem.bind( this );
	}
	
	componentDidMount() {				
		fetch( "/api/parecer/get/"+this.props.parecerId, {
			method : "GET",			
			credentials : "include",
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}							 
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				let codigo = 0;
				if ( resposta.status != 200 )					
					codigo = dados.codigo;	
				
				this.state.status = resposta.status;
				this.state.codigo = codigo;
				this.state.sucessoMsg = "Parecer carregado com êxito!";

				this.state.parecer = dados;
				
				this.setState( this.state );
				window.scrollTo( 0, 0 );
			} );			 
		} );
	}
	
	alterarMensagem( e ) {
		e.preventDefault();
		
		this.limpaMsgs();
		
		fetch( "/api/parecer/altera/"+this.props.parecerId, {
			method : "PATCH",
			credentials : "include",
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer " + sistema.token 
			},				
			body : JSON.stringify( { 
				mensagem : this.refs.mensagem.value
			} )
		} ).then( (resposta) => {
			this.state.status = resposta.status;
			if ( resposta.ok ) {				
				this.state.sucessoMsg = "Mensagem alterada com êxito!";											
				this.setState( this.state );
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
	
	limpaMsgs() {		
		this.state.status = 0;
		this.state.backendErroCodigo = 0;	
		this.state.sucessoMsg = null;
		this.state.erroMsg = null;
		
		this.setState( this.state );
	}
	
	render() {
		const { status, backendErroCodigo, parecer, sucessoMsg, erroMsg } = this.state;
 
 		if( parecer == null )
 			return ( <span></span> );
 
		return (
			<div className="container">
				<MensagemPainel tipo="backend-erro" status={status} codigo={backendErroCodigo} />				
				<MensagemPainel tipo="frontend-erro" msg={erroMsg} />				
				<MensagemPainel tipo="sucesso" msg={sucessoMsg} />
			
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6 card">
						<div className="card-body">
							<h5 className="card-title">Descrição do processo</h5>							
							<p>{parecer.processo.descricao}</p>
						</div>
					</div>
				</div>
				<br />
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6 card">
						<div className="card-body">
							<h5 className="card-title">Alteração de parecer</h5>
							<form onSubmit={this.alterarMensagem}>
								<div className="form-group">
									<label className="control-label" for="mensagem">Mensagem:</label>
									<br />
									<textarea defaultValue={parecer.mensagem} ref="mensagem" name="mensagem" className="form-control" />															
								</div>
								<div className="form-group">
									<input type="submit" value="Salvar" className="btn btn-primary col-sm-offset-2" />				
								</div>												
							</form>
						</div>
					</div>
				</div>
			</div>
		);
	}
	
}