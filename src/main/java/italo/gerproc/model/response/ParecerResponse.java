package italo.gerproc.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ParecerResponse {

	private Long id;
	
	private String mensagem;
	
	private String ultimaAlteracao;
	
	private boolean finalizado;
		
	private UsuarioResponse finalizador;
	
	private ProcessoResponse processo;
		
}
