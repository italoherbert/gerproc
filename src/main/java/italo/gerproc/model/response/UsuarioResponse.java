package italo.gerproc.model.response;

import italo.gerproc.model.UsuarioTipo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioResponse {

	private Long id;	
	
	private String nome;
	
	private String nomeUsuario;
		
	private UsuarioTipo tipo;
	
}
