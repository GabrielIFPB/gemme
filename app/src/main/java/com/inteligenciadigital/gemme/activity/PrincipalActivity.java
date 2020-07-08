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
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class PrincipalActivity extends AppCompatActivity {

	private MaterialCalendarView calendarView;
	private TextView nome, saldo;
	private FirebaseAuth firebaseAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);

		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);

		this.nome = findViewById(R.id.nome_id);
		this.saldo = findViewById(R.id.saldo_id);

		this.calendarView = findViewById(R.id.calendarView);
		this.configCalendario();

//		FloatingActionButton fab = findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//			}
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.sair) {
			this.firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
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