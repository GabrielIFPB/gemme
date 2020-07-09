package com.inteligenciadigital.gemme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.model.Movimentacao;

import java.util.List;

public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

	private List<Movimentacao> movimentacoes;
	private Context context;

	public AdapterMovimentacao(List<Movimentacao> movimentacoes, Context context) {
		this.movimentacoes = movimentacoes;
		this.context = context;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View item = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.adapter_movimentacao, parent, false);
		return new MyViewHolder(item);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		Movimentacao movimentacao = this.movimentacoes.get(position);

		holder.categoria.setText(movimentacao.getCategoria());
		holder.descricao.setText(movimentacao.getDescricao());

		if (movimentacao.getTipo() == 1) {
			holder.valor.setTextColor(this.context.getResources().getColor(R.color.colorReceita));
			holder.valor.setText(String.valueOf(movimentacao.getValor()));
		} else {
			holder.valor.setTextColor(this.context.getResources().getColor(R.color.colorDespesa));
			holder.valor.setText("-" + movimentacao.getValor());
		}
	}

	@Override
	public int getItemCount() {
		return this.movimentacoes.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView categoria;
		TextView descricao;
		TextView valor;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			valor = itemView.findViewById(R.id.recycler_valor_id);
			descricao = itemView.findViewById(R.id.recycler_descricao_id);
			categoria = itemView.findViewById(R.id.recycler_categoria_id);
		}
	}
}
