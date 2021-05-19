
class Sistema {
	
	constructor() {
		this.token = null;
		this.usuario = null;
	}
		
	scrollTo( strEL ) {
		let el = document.querySelector( "#"+strEL );
		if ( el )
			el.scrollIntoView();			
	}
		
}

const sistema = new Sistema();