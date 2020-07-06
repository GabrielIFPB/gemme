package com.inteligenciadigital.gemme.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class ConfiguracaoFirebase {
	private static FirebaseAuth FIREBASEAUTH;

	public static FirebaseAuth getFirebaseAuth() {
		if (FIREBASEAUTH == null)
			FIREBASEAUTH = FirebaseAuth.getInstance();
		return FIREBASEAUTH;
	}
}
