package italo.gerproc.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveUsuarioRequest {

	private String nome;
	
	private String nomeUsuario;
	
	private String senha;
	
	private String tipo;
	
}
