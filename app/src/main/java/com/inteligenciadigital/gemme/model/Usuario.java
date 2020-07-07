package com.inteligenciadigital.gemme.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;

public class Usuario {
	private String nome, email, senha, id;

	public Usuario() {
	}

	public Usuario(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public void salvar() {
		DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseData();
		firebase.child("usuarios")
				.child(this.id)
				.setValue(this);
	}

	@Exclude
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Exclude
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
