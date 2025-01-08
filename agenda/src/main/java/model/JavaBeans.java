package model;

public class JavaBeans {
	private String id;
	private String nome;
	private String phone;
	private String email;
	
	public JavaBeans() {
		super();
	}
	
	public JavaBeans(String id, String nome, String phone, String email) {
		super();
		this.id = id;
		this.nome = nome;
		this.phone = phone;
		this.email = email;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
