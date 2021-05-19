
class Naveg extends React.Component {
	
	constructor( props ) {
		super( props );
	}
	
	paraInicial() {
		if ( sistema.usuario.tipo == 'ADMIN' ) {
			ReactDOM.render( <AdminHome />, document.getElementById( "root" ) );
		} else if ( sistema.usuario.tipo == 'TRIADOR' ) {
			ReactDOM.render( <TriadorHome />, document.getElementById( "root" ) );
		} else {
			ReactDOM.render( <FinalizadorHome />, document.getElementById( "root" ) );
		}
	}
	
	paraLogin() {
		ReactDOM.render( <span></span>, document.getElementById( "naveg") );
		ReactDOM.render( <Login />, document.getElementById( "root" ) );
	}
	
	render() {
		return (
			<div className="container">
				<div className="row">
					<div className="col-sm-12">	
						<span className="font-weight-bold">Navegação: </span> 
						<button className="btn btn-link" onClick={(e) => this.paraInicial() }>
							Inicial
						</button>
						&gt;&gt;
						<button className="btn btn-link" onClick={(e) => this.paraLogin() }>
							logout
						</button>
					</div>
				</div>
			</div>
		);
	}
	
}