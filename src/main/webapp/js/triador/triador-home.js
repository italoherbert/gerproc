
class TriadorHome extends React.Component {
	
	constructor(props) {
		super(props);
	}
	
	render() {
		return (
			<div>
				<h3 className="text-center">Olá {sistema.usuario.nome}, bem vindo!</h3>								
				<br />
				<TriadorProcessos triadorId={sistema.usuario.id} />
			</div>
		);
	}
	
}