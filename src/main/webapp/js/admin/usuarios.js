
class Usuarios extends React.Component {
	
	constructor(props) {
		super(props);
		this.state = { 
			status : 0, 
			backendErroCodigo : 0, 
			usuarios : [],
			sucessoMsg : null,
			erroMsg : null
		};
		this.filtrar = this.filtrar.bind( this );
	}
	
	componentDidMount() {
		this.refs.nomeIni.value = "*";
		this.filtrar( null );
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();	
			
		this.limpaMsgs();
									
		fetch( "/api/usuario/filtra/"+this.refs.nomeIni.value, {
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
				this.state.usuarios = dados;
				this.state.sucessoMsg = "Usuários listados com êxito!";
				
				this.setState( this.state );
			} );			 
		} );		
	}
	
	editar( e, uid ) {
		this.limpaMsgs();
		
		ReactDOM.render( <UsuarioForm uid={uid} />, document.getElementById("root") );
	}
	
	acessar( e, uid ) {
		
	}
	
	remover( e, uid ) {
		this.limpaMsgs();
		
		fetch( "/api/usuario/deleta/"+uid, {
			method : "DELETE",			
			credentials : "include",
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}							 
		} ).then( (resposta) => {	
			this.state.status = resposta.status;

			if ( resposta.status == 200 ) {
				this.state.sucessoMsg = "Usuario removido com êxito!";				
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
	
	limpaMsgs() {		
		this.state.status = 0;
		this.state.backendErroCodigo = 0;	
		this.state.sucessoMsg = null;
		this.state.erroMsg = null;
		
		this.setState( this.state );
	}
	
	paraTelaRegistroUsuario( e ) {
		ReactDOM.render( <UsuarioForm />, document.getElementById( "root" ) );					
	}
	
	render() {
		const { status, backendErroCodigo, erroMsg, sucessoMsg, usuarios } = this.state;
		return (
			<div className="container">
				<div className="row">
					<h4 className="text-center col-md-12">Lista de usuários</h4>
					<table className="table table-striped col-md-12">
						<tr>
							<th>ID</th>
							<th>Nome</th>
							<th>Nome de usuário</th>
							<th>Tipo</th>
							<th>Editar</th>
							<th>Remover</th>
						</tr>
						{usuarios.map( ( usuario, index ) => {
							return (
								<tr>
									<td>{usuario.id}</td>
									<td>{usuario.nome}</td>
									<td>{usuario.nomeUsuario}</td>
									<td>{usuario.tipo}</td>
									<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.editar( e, usuario.id )}>editar</button></td>
									<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, usuario.id )}>remover</button></td>
								</tr>
							);
						} ) }								
					</table>
				</div>
				
				<MensagemPainel tipo="backend-erro" status={status} codigo={backendErroCodigo} />				
				<MensagemPainel tipo="frontend-erro" msg={erroMsg} />				
				<MensagemPainel tipo="sucesso" msg={sucessoMsg} />
				
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6">
						<form onSubmit={this.filtrar}>
							<div className="form-group">
								<label className="control-label" for="nomeIni">Nome do usuário:</label>
								<input type="text" ref="nomeIni" name="nomeIni" className="form-control" />						
							</div>
														
							<div className="form-group">
								<input type="submit" value="Filtrar" className="col-sm-offset-2" />				
							</div>		
						</form>	
					</div>
				</div>	
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6">
						<button className="btn btn-link" style={{ padding : 0 }} onClick={ (e) => this.paraTelaRegistroUsuario(e)}>Registrar novo usuário</button>	
					</div>
				</div>				 
			</div>
		);
	}
	
}