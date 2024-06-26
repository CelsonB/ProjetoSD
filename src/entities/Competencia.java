package entities;

public class Competencia {
	
	private String nomeCompetencia;
	private int experiencia;
	
	
	public Competencia(int experiencia,String nomeCompetencia) {
		this.experiencia = experiencia;
		this.nomeCompetencia = nomeCompetencia;
	}

	public String getNomeCompetencia() {
		return nomeCompetencia;
	}

	public void setNomeCompetencia(String nomeCompetencia) {
		this.nomeCompetencia = nomeCompetencia;
	}

	public int getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(int experiencia) {
		this.experiencia = experiencia;
	}
	
	
	
	
}
