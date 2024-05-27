package entities;

public class Empresa {
	
	private String nome; 
	private String ramo;
	private String email;
	private String razaoSocial;
	private String senha;
	private String descricao;
	private String cnpj;
	public Empresa(String nome, String email, String senha, String razaoSocial, String descricao, String ramo,String cnpj) {
		super();
		this.nome = nome;
		this.ramo = ramo;
		this.email = email;
		this.razaoSocial = razaoSocial;
		this.senha = senha;
		this.descricao = descricao;
		this.cnpj = cnpj; 
	}
	
	
	
	public Empresa() {
		super();
	}



	public String getCnpj() {
		return cnpj;
	}



	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}



	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRamo() {
		return ramo;
	}
	public void setRamo(String ramo) {
		this.ramo = ramo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razao_social) {
		this.razaoSocial = razao_social;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
			
			
			
			
	
	

}
