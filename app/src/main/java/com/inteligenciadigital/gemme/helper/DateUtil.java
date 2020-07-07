package com.inteligenciadigital.gemme.helper;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class DateUtil {
	public static String getDateNow() {
		long date = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}

	public static String getMesAno(String date) {
		String[] retorno = date.split("/");
		return retorno[1] + retorno[2];
	}
}
