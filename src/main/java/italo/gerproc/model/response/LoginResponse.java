package italo.gerproc.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {

	private UsuarioResponse usuario;
	
	public String token;
	
}
