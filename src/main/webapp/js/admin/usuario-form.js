
class UsuarioForm extends React.Component {

	constructor(props) {
		super(props);
		this.state = { uid : -1, tipo : 'ADMIN', status : 0, backendErroCodigo : 0, sucessoMsg : null, erroMsg : null };
		this.salvar = this.salvar.bind(this);
		this.trataTipoAlterado = this.trataTipoAlterado.bind(this);
	}
	
	componentDidMount() {
		if ( this.props.uid == null && this.props.uid == undefined )
			return;
			
		this.state.uid = this.props.uid;
																
		fetch( "/api/usuario/get/"+this.props.uid, {
			method : "GET",
			credentials : "include",
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer " + sistema.token 
			}
		} ).then( (resposta) => {
			this.state.status = resposta.status;
			resposta.json().then( (dados) => {
				if ( resposta.ok ) {
					this.refs.nome.value = dados.nome;
					this.refs.nomeUsuario.value = dados.nomeUsuario;
					this.refs.senha.value = dados.senha;
					this.refs.senha2.value = dados.senha;
					
					this.state.tipo = dados.tipo;						
				} else if ( resposta.status == 400 ) {
					this.state.backendErroCodigo = dados.codigo;
				} 
				
				this.setState( this.state );
			} );	
		} );			
	}
	
	salvar(e) {		
		e.preventDefault();
		
		this.limpaMsgs();
		
		if ( this.refs.senha.value != this.refs.senha2.value ) {
			this.state.erroMsg = "A confirmação de senha está diferente da senha informada.";
			
			this.setState( this.state );
			return;
		}
		
		let url;
		let method;
		
		if ( this.state.uid == -1 ) {
			url = "/api/usuario/registra";
			method = "POST";
		} else {
			url = "/api/usuario/atualiza/"+this.state.uid;
			method = "PUT";
		}
				
		fetch( url, {
			method : method,
			credentials : "include",
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer " + sistema.token 
			},				
			body : JSON.stringify( { 
				nome : this.refs.nome.value,
				nomeUsuario : this.refs.nomeUsuario.value,
				senha : this.refs.senha.value,
				tipo : this.state.tipo
			} )
		} ).then( (resposta) => {
			this.state.status = resposta.status;
			if ( resposta.ok ) {
				this.state.sucessoMsg = "Usuário salvo com êxito!";											
				this.setState( this.state )
			} else {
				resposta.json().then( (dados) => {
					if ( resposta.status == 400 )
						this.state.backendErroCodigo = dados.codigo; 
					
					this.setState( this.state );
				} );
			}		
		} );	
	}	
		
	trataTipoAlterado (e) {
		this.state.tipo = e.target.value;
		this.setState( this.state );				
	}
	
	paraListaUsuariosTela( e ) {
		ReactDOM.render( <Usuarios />, document.getElementById("root") );
	}
	
	limpaMsgs() {		
		this.state.status = 0;
		this.state.backendErroCodigo = 0;	
		this.state.sucessoMsg = null;
		this.state.erroMsg = null;
		
		this.setState( this.state );
	}
	
	render() {	
		const { tipo, status, backendErroCodigo, sucessoMsg, erroMsg } = this.state;
		return (
			<div className="container">
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6">
						<div className="card">
							<div className="card-body">
								<h4 className="card-title">Formulário de usuário</h4>
									
								<MensagemPainel tipo="backend-erro" status={status} codigo={backendErroCodigo} />				
								<MensagemPainel tipo="frontend-erro" msg={erroMsg} />				
								<MensagemPainel tipo="sucesso" msg={sucessoMsg} />	
									
								<form onSubmit={this.salvar} className="form-horizontal">
									<div className="form-group">
										<label className="control-label" for="nome">Nome:</label>
										<br />
										<input type="text" ref="nome" name="nome" className="form-control" />						
									</div>
									<div className="form-group">
										<label className="control-label" for="nomeUsuario">Nome de usuário</label>
										<br />
										<input type="text" ref="nomeUsuario" name="nomeUsuario" className="form-control" />						
									</div>
									<div className="form-group">
										<label className="control-label" for="senha">Senha</label>
										<br />
										<input type="password" ref="senha" name="senha" className="form-control" />						
									</div>
									
									<div className="form-group">
										<label className="control-label" for="senha2">Repita a senha</label>
										<br />
										<input type="password" ref="senha2" name="senha2" className="form-control" />						
									</div>
									
									<div className="form-group">
										<label className="control-label">Tipo de usuário: </label>
										<div style={{ marginLeft: 15 }}>
											<input type="radio" value="ADMIN" checked={tipo === 'ADMIN'} onChange={this.trataTipoAlterado} />Administrador<br /> 
											<input type="radio" value="TRIADOR" checked={tipo === 'TRIADOR'} onChange={this.trataTipoAlterado} />Triador<br />
											<input type="radio" value="FINALIZADOR" checked={tipo === 'FINALIZADOR'} onChange={this.trataTipoAlterado} />Finalizador
										</div>
									</div>
										
									<div className="form-group">
										<input type="submit" value="Salvar" className="btn btn-primary col-sm-offset-2" />				
									</div>														
									
									<button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.paraListaUsuariosTela( e ) }>Tela de usuários</button>		
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>										
		);
	}
}
