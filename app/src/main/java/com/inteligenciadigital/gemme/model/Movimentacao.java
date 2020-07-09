package com.inteligenciadigital.gemme.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;
import com.inteligenciadigital.gemme.helper.Base64Custom;
import com.inteligenciadigital.gemme.helper.DateUtil;

import java.util.Objects;

public class Movimentacao {
	private String id, data, categoria, descricao;
	private int tipo;
	private Double valor;

	public Movimentacao() {
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void salvar() {
		FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
		String idUser = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()));

		DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseData();

		firebase.child("movimentacao")
				.child(idUser)
				.child(DateUtil.getMesAno(this.getData()))
				.push()
				.setValue(this);
	}

	@Exclude
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
