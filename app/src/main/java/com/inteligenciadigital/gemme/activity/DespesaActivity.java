package com.inteligenciadigital.gemme.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;
import com.inteligenciadigital.gemme.helper.Base64Custom;
import com.inteligenciadigital.gemme.helper.DateUtil;
import com.inteligenciadigital.gemme.model.Movimentacao;
import com.inteligenciadigital.gemme.model.Usuario;

public class DespesaActivity extends AppCompatActivity {

    private EditText valor;
    private TextInputEditText data, categoria, descricao;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseData = ConfiguracaoFirebase.getFirebaseData();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
    private Double despesaTotal, despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        this.categoria = findViewById(R.id.categoria_id);
        this.descricao = findViewById(R.id.descricao_id);
        this.data = findViewById(R.id.data_id);
        this.valor = findViewById(R.id.valor_id);

        this.data.setText(DateUtil.getDateNow());

        this.getDespesaTotal();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void salvar(View view) {
        if (this.validarInputs()) {
            this.movimentacao = new Movimentacao();
            this.movimentacao.setCategoria(this.categoria.getText().toString());
            this.movimentacao.setDescricao(this.descricao.getText().toString());
            this.movimentacao.setData(this.data.getText().toString());

            double valor = Double.parseDouble(this.valor.getText().toString());

            this.movimentacao.setValor(valor);
            this.movimentacao.setTipo(2);

            this.despesaAtualizada = this.despesaTotal + valor;

            this.atualizarDespesa();
            this.movimentacao.salvar();

            this.limpar();
        }
    }

    private boolean validarInputs() {
        boolean isValid = true;
        String categoria = this.categoria.getText().toString();
        String descricao = this.descricao.getText().toString();
        String valor = this.valor.getText().toString();
        String data = this.data.getText().toString();
        if (categoria.isEmpty()) {
            Toast.makeText(DespesaActivity.this,
                    "Preencha a categoria!",
                    Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (descricao.isEmpty()) {
            Toast.makeText(DespesaActivity.this,
                    "Preencha a descrição!",
                    Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (valor.isEmpty()) {
            Toast.makeText(DespesaActivity.this,
                    "Preencha o valor!",
                    Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (data.isEmpty()) {
            Toast.makeText(DespesaActivity.this,
                    "Preencha a data!",
                    Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private void limpar() {
        this.categoria.setText("");
        this.descricao.setText("");
        this.data.setText(DateUtil.getDateNow());
        this.valor.setText("");

        Toast.makeText(DespesaActivity.this,
                "Despesa salvo com sucesso!",
                Toast.LENGTH_SHORT).show();
    }

    private void getDespesaTotal() {
        String idUser = Base64Custom.codificarBase64(this.firebaseAuth.getCurrentUser().getEmail());
        DatabaseReference usuarioDb = this.firebaseData.child("usuarios")
                .child(idUser);

        usuarioDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario user = snapshot.getValue(Usuario.class);
                despesaTotal = user.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void atualizarDespesa() {
        String idUser = Base64Custom.codificarBase64(this.firebaseAuth.getCurrentUser().getEmail());
        DatabaseReference usuarioDb = this.firebaseData.child("usuarios")
                .child(idUser);

        usuarioDb.child("despesaTotal").setValue(this.despesaAtualizada);
    }
}