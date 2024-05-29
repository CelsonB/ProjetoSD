package entities;

import java.util.UUID;

public class SessaoEmpresa extends Empresa {

	
	
 	public SessaoEmpresa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SessaoEmpresa( String email, String senha, String razaoSocial, String descricao, String ramo, String cnpj) {
		super( email, senha, razaoSocial, descricao, ramo, cnpj);
	}

	public UUID getToken() {
		return token;
	}

	public void setToken(UUID token) {
		this.token = token;
	}

	private UUID token;
}
