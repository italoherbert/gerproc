package italo.gerproc.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveProcessoRequest {

	private String descricao;
	
	private Long triadorId;	
	
	private Long[] finalizadoresIds;
	
}
