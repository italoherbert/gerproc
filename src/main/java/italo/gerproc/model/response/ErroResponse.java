package italo.gerproc.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErroResponse {
		
	public final static int SER_CRIADOR_REQUERIDO = 1; 

	
	public final static int USUARIO_NAO_ENCONTRADO = 100;
	public final static int USUARIO_JA_EXISTE = 101;	
	public final static int USUARIO_NOME_REQUERIDO = 102;
	public final static int USUARIO_NOME_USUARIO_REQUERIDO = 103;
	public final static int USUARIO_SENHA_REQUERIDA = 104;
	public final static int USUARIO_TIPO_REQUERIDO = 105;
	public final static int USUARIO_TIPO_DESCONHECIDO = 106;
	
	public final static int PROCESSO_NAO_ENCONTRADO = 200; 
	public final static int PROCESSO_DESCRICAO_REQUERIDA = 201;
	public final static int PROCESSO_FINALIZADOR_REQUERIDO = 202;
		
	public final static int PARECER_NAO_ENCONTRADO = 300;
	public final static int PARECER_MENSAGEM_REQUERIDA = 301;
			
	private final int codigo;
	
}
