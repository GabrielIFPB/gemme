package com.inteligenciadigital.gemme;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

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
				.build()
		);
	}
}