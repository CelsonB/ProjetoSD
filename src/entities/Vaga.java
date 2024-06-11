package entities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

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
	
	public void setCompetencias(String competencias) {
		
		ArrayList<String > comps = new ArrayList<>();
		try {
			JSONArray jsonarr = new JSONArray(competencias.replace("#", "sharp"));
		
			for(int i =0; i<jsonarr.length();i++) {
				
					comps.add(jsonarr.getString(i).replace("sharp", "#"));
			}
			
			
			} catch (JSONException e) {
			}
		this.competencias = comps;
	}
	
	@Override
	public String toString() {
		return "Vaga [nome=" + nome + ", idVaga=" + idVaga+"]";
	}
	 
	
	
}
