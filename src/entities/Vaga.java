package entities;

import java.util.ArrayList;

public class Vaga {

	
	public Vaga() {
		super();
		
	}
	public Vaga(String nome, int id) {
		super();
		this.idVaga = id;
		this.nome = nome;
	}
	private String nome;
	private int idVaga;
	private String email;
	private float faixaSalarial;
	private String descricao;
	private String estado;
	private ArrayList<String> competencias;
	
	
	public int getIdVaga() {
		return idVaga;
	}
	public void setIdVaga(int idVaga) {
		this.idVaga = idVaga;
	}
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
	@Override
	public String toString() {
		return "Vaga [nome=" + nome + ", idVaga=" + idVaga+"]";
	}
	 
	
	
}
