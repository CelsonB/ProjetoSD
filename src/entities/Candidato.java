package entities;

import java.util.List;
import java.util.UUID;

public class Candidato {

	
	
	private String nome;
	private String senha;
	private String email;
	private int id;
	
	private UUID token =null;
	private List<Competencia> listaCompetencia;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Competencia> getListaCompetencia() {
		return listaCompetencia;
	}

	public void setListaCompetencia(List<Competencia> listaCompetencia) {
		this.listaCompetencia = listaCompetencia;
	}

	public Candidato(String nome, String senha, String email, UUID token) {
		super();
		this.nome = nome;
		this.senha = senha;
		this.email = email;
		this.token = token;
	}
	
	public Candidato(String nome, String senha, String email) {
		super();
		this.nome = nome;
		this.senha = senha;
		this.email = email;
		
	}
	
	
	public Candidato() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UUID getToken() {
		return token;
	}

	public void setToken(UUID token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Candidato [nome=" + nome + ", senha=" + senha + ", email=" + email + ", token=" + token + "]";
	}
	
	
}
