package com.inteligenciadigital.gemme.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;
import com.inteligenciadigital.gemme.helper.Base64Custom;
import com.inteligenciadigital.gemme.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;

public class PrincipalActivity extends AppCompatActivity {

	private MaterialCalendarView calendarView;
	private TextView textNome, textSaldo;
	private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
	private DatabaseReference firebaseData = ConfiguracaoFirebase.getFirebaseData();

	private Double receitaTotal = 0.0, despesaTotal = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);

		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);

		this.textNome = findViewById(R.id.nome_id);
		this.textSaldo = findViewById(R.id.saldo_id);

		this.calendarView = findViewById(R.id.calendarView);
		this.configCalendario();
		this.getSaldoGeral();

	}

	private void getSaldoGeral() {
		String idUser = Base64Custom.codificarBase64(this.firebaseAuth.getCurrentUser().getEmail());
		DatabaseReference usuarioDb = this.firebaseData.child("usuarios")
				.child(idUser);

		usuarioDb.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Usuario user = snapshot.getValue(Usuario.class);
				despesaTotal = user.getDespesaTotal();
				receitaTotal = user.getReceitaTotal();

				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

				textNome.setText("Olá, " + user.getNome());
				textSaldo.setText("R$ " + decimalFormat.format(receitaTotal - despesaTotal));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.sair) {
			this.firebaseAuth.signOut();
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public void addReceita(View view) {
		startActivity(new Intent(this, ReceitaActivity.class));
	}

	public void addDespesa(View view) {
		startActivity(new Intent(this, DespesaActivity.class));
	}

	private void configCalendario() {
		CharSequence[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
		this.calendarView.setTitleMonths(meses);

		this.calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
			@Override
			public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
				Toast.makeText(PrincipalActivity.this, date.getDay() + "/" + date.getMonth() + "/" + date.getYear(), Toast.LENGTH_LONG).show();
			}
		});
	}
}