
class MensagemPainel extends React.Component {
	
	constructor(props) {
		super(props);
		this.state = { msgId : "msg-"+new Date().getTime() };
	}		
	
	componentDidMount() {
		sistema.scrollTo( this.state.msgId );
	}
			
	render () {				
		const {msgId} = this.state;
		
		let className = "alert ";		
			
		let mensagem = null;
		if ( this.props.tipo == "sucesso" ) {
			mensagem = this.props.msg;
			className += "alert-success";
		} else if ( this.props.tipo == "frontend-erro" ) {
			mensagem = this.props.msg;
			className += "alert-danger";
		} else if ( this.props.tipo == "backend-erro" ){
			mensagem = this.getBackendErro();
			className += "alert-danger";
		}
		
		if ( mensagem == null ) {
			return (<span></span>);
		} else {
			return (
				<div id={msgId} className={className}>
					{mensagem}
				</div>
			);
		}
	}
				
	getBackendErro() {		
		switch ( this.props.status ) {
			case 400:
				switch( this.props.codigo ) {
					case 1: return "Você não é o criador do recurso.";
					
					case 100: return "Usuario não encontrado.";					
					case 101: return "Usuario já existe cadastrado com nome de usuário informado.";
					case 102: return "O nome do usuário é um campo requerido.";
					case 103: return "O nome de usuário é um campo requerido.";
					case 104: return "A senha do usuário é um campo requerido.";
					case 105: return "O tipo do usuário é um campo requerido.";
					case 106: return "Tipo de usuário inválido.";
						
					case 200: return "Processo não encontrado.";
					case 201: return "A descrição do processo é um campo requerido.";
					case 202: return "É necessário vincular ao menos um usuário finalizador ao processo.";
						
					case 300: return "Parecer não encontrado.";
					case 301: return "A mensagem do parecer é um campo requerido.";
					
					default: return null;
				}
	  		case 401: return "É necessária autenticação para utilizar esse recurso!";
			case 403: return "Você não tem autorização para utilizar esse recurso!";	
	  		default: return null;
		}
	}
		
}
		