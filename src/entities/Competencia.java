package entities;

public class Competencia {
	
	private String nomeCompetencia;
	private String experiencia;
	
	
	public Competencia(String experiencia,String nomeCompetencia) {
		this.experiencia = experiencia;
		this.nomeCompetencia = nomeCompetencia;
	}

	public String getNomeCompetencia() {
		return nomeCompetencia;
	}

	public void setNomeCompetencia(String nomeCompetencia) {
		this.nomeCompetencia = nomeCompetencia;
	}

	public String getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(String experiencia) {
		this.experiencia = experiencia;
	}
	
	
	
}
