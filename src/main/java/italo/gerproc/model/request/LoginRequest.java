package italo.gerproc.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {

	public String nomeUsuario;
	
	private String senha;
	
}
