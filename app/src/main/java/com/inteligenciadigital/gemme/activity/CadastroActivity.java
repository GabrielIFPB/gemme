package com.inteligenciadigital.gemme.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;
import com.inteligenciadigital.gemme.helper.Base64Custom;
import com.inteligenciadigital.gemme.model.Usuario;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

//	private Button btn;
	private EditText nome, email, senha;
	private FirebaseAuth firebaseAuth;
	private Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);

		getSupportActionBar().setTitle("Cadastro");

//		this.btn = findViewById(R.id.btn_id);
		this.email = findViewById(R.id.email_id);
		this.senha = findViewById(R.id.senha_id);
		this.nome = findViewById(R.id.nome_id);
	}

	private boolean inputIsEmpty(String nome, String email, String senha) {
		boolean isValid = true;
		if (nome.isEmpty()) {
			Toast.makeText(
					CadastroActivity.this,
					"Preencha o nome!",
					Toast.LENGTH_SHORT).show();
			isValid = false;
		} else if (email.isEmpty()) {
			Toast.makeText(
					CadastroActivity.this,
					"Preencha o e-mail!",
					Toast.LENGTH_SHORT).show();
			isValid = false;
		} else if (senha.isEmpty()) {
			Toast.makeText(
					CadastroActivity.this,
					"Preencha o senha!",
					Toast.LENGTH_SHORT).show();
			isValid = false;
		}
		return isValid;
	}

	public void cadastrar(View view) {
		String nome = this.nome.getText().toString();
		String email = this.email.getText().toString();
		String senha = this.senha.getText().toString();
		if (this.inputIsEmpty(nome, email, senha)) {
			this.usuario = new Usuario();
			this.usuario.setNome(nome);
			this.usuario.setEmail(email);
			this.usuario.setSenha(senha);
			this.firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
			this.firebaseAuth.createUserWithEmailAndPassword(
					this.usuario.getEmail(), this.usuario.getSenha()
			).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@RequiresApi(api = Build.VERSION_CODES.KITKAT)
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if (task.isSuccessful()) {
						String idUser = Base64Custom.codificarBase64(usuario.getEmail());
						usuario.setId(idUser);
						usuario.salvar();
						finish();
					} else {
						String excecao = "";
						try {
							throw Objects.requireNonNull(task.getException());
						} catch (FirebaseAuthWeakPasswordException e) {
							excecao = "Digite uma senha mais forte!";
						} catch (FirebaseAuthInvalidCredentialsException e) {
							excecao = "Por favor, digite um e-mail válido!";
						} catch (FirebaseAuthUserCollisionException e) {
							excecao = "Está conta já foi cadastrada!";
						} catch (Exception e) {
							excecao = "Erro ao cadastrar usuário: " + e.getMessage();
							e.printStackTrace();
						}
						Toast.makeText(
								CadastroActivity.this,
								excecao,
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
}