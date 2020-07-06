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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;
import com.inteligenciadigital.gemme.model.Usuario;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText email, senha;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.email = findViewById(R.id.email_id);
        this.senha = findViewById(R.id.senha_id);
    }

    private boolean inputIsEmpty(String email, String senha) {
        boolean isValid = true;
        if (email.isEmpty()) {
            Toast.makeText(
                    LoginActivity.this,
                    "Preencha o e-mail!",
                    Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (senha.isEmpty()) {
            Toast.makeText(
                    LoginActivity.this,
                    "Preencha o senha!",
                    Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    public void login(View view) {
        String email = this.email.getText().toString();
        String senha = this.senha.getText().toString();
        if (this.inputIsEmpty(email, senha)) {
            this.usuario = new Usuario();
            this.usuario.setEmail(email);
            this.usuario.setSenha(senha);
            this.firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
            this.firebaseAuth.signInWithEmailAndPassword(
                    this.usuario.getEmail(), this.usuario.getSenha()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                                LoginActivity.this,
                                "Sucesso ao fazer login!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String excecao = "";
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidUserException e) {
                            excecao = "Usuário não está cadastrado.";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "E-mail e senha não correspondem a um usuário cadastrado.";
                        } catch (Exception e) {
                            excecao = "Erro ao fazer login: " + e.getMessage();
                            e.printStackTrace();
                        }
                        Toast.makeText(
                                LoginActivity.this,
                                excecao,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}