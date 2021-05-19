package italo.gerproc.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProcessoResponse {

	private Long id;
	
	private String descricao;
	
	private String ultimaAlteracao;
	
	private UsuarioResponse triador;
	
	private List<ParecerResponse> pareceres;
		
}
