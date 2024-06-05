package entities;

import java.util.ArrayList;

public class Vaga {
//	{
//		  "operacao": "cadastrarVaga",
//		  "nome":"xxxx",
//		  "email":"xx@xxx.xxx",
//		  "faixaSalarial":12345,
//		  "descricao":"xxxxx",
//		  "estado":"xxxxx",
//		  "competencias": ["xxxx","xxxx","xxxx"],
//		  "token":"UUID"
//		}
	
	public Vaga() {
		super();
	}
	private String nome;
	
	private String email;
	private float faixaSalarial;
	private String descricao;
	private String estado;
	private ArrayList<String> competencias;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public float getFaixaSalarial() {
		return faixaSalarial;
	}
	public void setFaixaSalarial(float faixaSalarial) {
		this.faixaSalarial = faixaSalarial;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public ArrayList<String> getCompetencias() {
		return competencias;
	}
	public void setCompetencias(ArrayList<String> competencias) {
		this.competencias = competencias;
	}
	 
	
	
}
