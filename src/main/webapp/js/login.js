
class Login extends React.Component {

	constructor(props) {
		super(props);
		this.state = { status : 0, backendErroCodigo : 0 };
		this.entrar = this.entrar.bind(this);
	}
	
	entrar(e) {		
		e.preventDefault();	
			
		this.setState( { status : 0, backendErroCodigo : 0 } );

		fetch( "/api/login", {
			method : "POST",			
			credentials : "include",
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
			},				
			body : JSON.stringify( { 
				nomeUsuario : this.refs.nomeUsuario.value,
				senha : this.refs.senha.value
			} )
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					sistema.token = dados.token;
					sistema.usuario = dados.usuario;
					
					ReactDOM.render( <Naveg />, document.getElementById( "naveg" ) );
															
					if ( dados.usuario.tipo == 'ADMIN' ) {
						ReactDOM.render( <AdminHome />, document.getElementById( "root" ) );
					} else if ( dados.usuario.tipo == 'TRIADOR' ) {
						ReactDOM.render( <TriadorHome />, document.getElementById( "root" ) );
					} else {
						ReactDOM.render( <FinalizadorHome />, document.getElementById( "root" ) );
					}					
				} else if ( resposta.status == 400 ) {
					this.state.backendErroCodigo = dados.codigo;	
				}
				this.setState( this.state )
			} );			 
		} );
	}
	
	
	render() {
		const { status, backendErroCodigo } = this.state;
						
		return (
			<div className="container">
				<div className="row">
					<div className="col-md-4"></div>
					<div className="card col-md-4">
						<div className="card-body">
							<h4 className="card-title">Tela de login</h4>
													
							<form onSubmit={this.entrar}>
								<div className="form-group">
									<label className="control-label" for="nomeUsuario">Nome de usuário</label>
									<input type="text" ref="nomeUsuario" name="nomeUsuario" className="form-control" />						
								</div>
								<div className="form-group">
									<label className="control-label" for="senha">Senha</label>
									<input type="password" ref="senha" name="senha" className="form-control" />						
								</div>
									
								<div className="form-group">
									<input type="submit" value="Entrar" className="btn btn-primary col-sm-offset-2" />				
								</div>
				
								<MensagemPainel tipo="backend-erro" status={status} codigo={backendErroCodigo} />									
							</form>	
						</div>
					</div>
				</div>			
			</div>									
		);
	}
}
