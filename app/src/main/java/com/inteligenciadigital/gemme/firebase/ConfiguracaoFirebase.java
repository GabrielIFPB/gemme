package com.inteligenciadigital.gemme.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
	private static FirebaseAuth FIREBASEAUTH;
	private static DatabaseReference FIREBASE;

	public static FirebaseAuth getFirebaseAuth() {
		if (FIREBASEAUTH == null)
			FIREBASEAUTH = FirebaseAuth.getInstance();
		return FIREBASEAUTH;
	}

	public static DatabaseReference getFirebaseData() {
		if (FIREBASE == null)
			FIREBASE = FirebaseDatabase.getInstance().getReference();
		return FIREBASE;
	}
}
