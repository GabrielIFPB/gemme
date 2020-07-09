package com.inteligenciadigital.gemme.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.adapter.AdapterMovimentacao;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;
import com.inteligenciadigital.gemme.helper.Base64Custom;
import com.inteligenciadigital.gemme.model.Movimentacao;
import com.inteligenciadigital.gemme.model.Usuario;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

	private MaterialCalendarView calendarView;
	private TextView textNome, textSaldo;

	private RecyclerView recyclerView;
	private AdapterMovimentacao adapterMovimentacao;
	private List<Movimentacao> movimentacoes = new ArrayList<>();

	private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
	private DatabaseReference firebaseData = ConfiguracaoFirebase.getFirebaseData();
	private DatabaseReference usuarioDb, movimentacaoDb;
	private ValueEventListener valueEventListenerUser, valueEventListenerMovi;

	private Movimentacao movimentacao;

	private String data;
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

		this.recyclerView = findViewById(R.id.list_movimento_id);

		this.calendarView = findViewById(R.id.calendarView);
		this.configCalendario();
		this.swipe();

		this.adapterMovimentacao = new AdapterMovimentacao(this.movimentacoes, this);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		this.recyclerView.setLayoutManager(layoutManager);
		this.recyclerView.setHasFixedSize(true);
		this.recyclerView.setAdapter(this.adapterMovimentacao);
	}

	private void swipe() {
		ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
			@Override
			public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
				int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
				int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
				return makeMovementFlags(dragFlags, swipeFlags);
			}

			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				excluirMovimentacao(viewHolder);
			}
		};

		new ItemTouchHelper(itemTouch).attachToRecyclerView(this.recyclerView);
	}

	private void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
		AlertDialog.Builder alert  = new AlertDialog.Builder(this);
		alert.setTitle("Excluir Movimentação da Conta");
		alert.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?");
		alert.setCancelable(false);

		alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int position = viewHolder.getAdapterPosition();
				movimentacao = movimentacoes.get(position);
				String idUser = Base64Custom.codificarBase64(firebaseAuth.getCurrentUser().getEmail());
				movimentacaoDb = firebaseData.child("movimentacao")
						.child(idUser)
						.child(data);

				movimentacaoDb.child(movimentacao.getId()).removeValue();
				adapterMovimentacao.notifyItemRemoved(position);
				atualizarSaldo();
			}
		});

		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adapterMovimentacao.notifyDataSetChanged();
			}
		});

		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	private void atualizarSaldo() {
		String idUser = Base64Custom.codificarBase64(this.firebaseAuth.getCurrentUser().getEmail());
		this.usuarioDb = this.firebaseData.child("usuarios")
				.child(idUser);

		if (this.movimentacao.getTipo() == 1) {
			this.receitaTotal -= this.movimentacao.getValor();
			this.usuarioDb.child("receitaTotal").setValue(this.receitaTotal);
		} else {
			this.despesaTotal -= this.movimentacao.getValor();
			this.usuarioDb.child("despesaTotal").setValue(this.despesaTotal);
		}
	}

	private void getMovimentacoes() {
		String idUser = Base64Custom.codificarBase64(this.firebaseAuth.getCurrentUser().getEmail());
		this.movimentacaoDb = this.firebaseData.child("movimentacao")
				.child(idUser)
				.child(this.data);

		this.valueEventListenerMovi = this.movimentacaoDb.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				movimentacoes.clear();
				for (DataSnapshot dados: snapshot.getChildren()) {
					Movimentacao movimentacao = dados.getValue(Movimentacao.class);
					movimentacao.setId(dados.getKey());
					movimentacoes.add(movimentacao);
				}
				adapterMovimentacao.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void getSaldoGeral() {
		String idUser = Base64Custom.codificarBase64(this.firebaseAuth.getCurrentUser().getEmail());
		this.usuarioDb = this.firebaseData.child("usuarios")
				.child(idUser);

		this.valueEventListenerUser = this.usuarioDb.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Usuario user = snapshot.getValue(Usuario.class);
				despesaTotal = user.getDespesaTotal();
				receitaTotal = user.getReceitaTotal();

				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

				textNome.setText("Olá, " + user.getNome() + "!");
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

		CalendarDay dataAtual = calendarView.getCurrentDate();

		this.data = String.format("%02d", dataAtual.getMonth()) + dataAtual.getYear();

		this.calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
			@Override
			public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
				data = String.format("%02d", date.getMonth()) + date.getYear();
				movimentacaoDb.removeEventListener(valueEventListenerMovi);
				getMovimentacoes();
			}
		});
	}

	@Override
	protected void onStart() {
		this.getSaldoGeral();
		this.getMovimentacoes();
		super.onStart();
	}

	@Override
	protected void onStop() {
		this.usuarioDb.removeEventListener(this.valueEventListenerUser);
		this.movimentacaoDb.removeEventListener(this.valueEventListenerMovi);
		super.onStop();
	}
}