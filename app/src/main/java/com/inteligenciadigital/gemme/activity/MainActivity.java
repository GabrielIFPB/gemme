package com.inteligenciadigital.gemme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.activity.CadastroActivity;
import com.inteligenciadigital.gemme.activity.LoginActivity;

public class MainActivity extends IntroActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

		setButtonBackVisible(false);
		setButtonNextVisible(false);

		addSlide(new FragmentSlide.Builder()
				.background(android.R.color.white)
				.fragment(R.layout.slide_1)
				.build()
		);

		addSlide(new FragmentSlide.Builder()
				.background(android.R.color.white)
				.fragment(R.layout.slide_2)
				.build()
		);

		addSlide(new FragmentSlide.Builder()
				.background(android.R.color.white)
				.fragment(R.layout.slide_3)
				.build()
		);

		addSlide(new FragmentSlide.Builder()
				.background(android.R.color.white)
				.fragment(R.layout.slide_4)
//				.canGoBackward(false)
//				.canGoForward(false)
				.build()
		);

		addSlide(new FragmentSlide.Builder()
						.background(android.R.color.white)
						.fragment(R.layout.slide_cadastro)
//				.canGoBackward(false)
//				.canGoForward(false)
						.build()
		);

	}

	public void btnLogin(View view) {
		startActivity(new Intent(this, LoginActivity.class));
	}

	public void btnCadastrar(View view) {
		startActivity(new Intent(this, CadastroActivity.class));
	}
}