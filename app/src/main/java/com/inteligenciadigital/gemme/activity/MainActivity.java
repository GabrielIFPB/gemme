package com.inteligenciadigital.gemme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.inteligenciadigital.gemme.R;
import com.inteligenciadigital.gemme.firebase.ConfiguracaoFirebase;

public class MainActivity extends IntroActivity {

	private FirebaseAuth firebaseAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

		this.isLogado();

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
				.canGoForward(false)
						.build()
		);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.isLogado();
//		this.firebaseAuth.signOut();
	}

	public void btnLogin(View view) {
		startActivity(new Intent(this, LoginActivity.class));
	}

	public void btnCadastrar(View view) {
		startActivity(new Intent(this, CadastroActivity.class));
	}

	public void isLogado() {
		this.firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
		if (this.firebaseAuth.getCurrentUser() != null) {
			this.activityPrincipal();
		}
	}

	public void activityPrincipal() {
		startActivity(new Intent(this, PrincipalActivity.class));
	}
}