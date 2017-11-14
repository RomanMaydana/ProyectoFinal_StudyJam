package com.maydana.roman.miepisodio;

import android.content.SharedPreferences;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AcercaDeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @ColorInt
    private int backgroundColor;
    private LinearLayout linearLayout;
    private ImageView logoImageView,tituloImageView;
    private TextView desarrolladoTxt,contactoTxt,ciudadTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        logoImageView = (ImageView)findViewById(R.id.logoImagenView);
        tituloImageView = (ImageView)findViewById(R.id.tituloImagenView);
        desarrolladoTxt = (TextView)findViewById(R.id.desarroladoTxt);
        contactoTxt = (TextView)findViewById(R.id.contactoTxt);
        ciudadTxt = (TextView)findViewById(R.id.ciudadTxt);
        setupSharedPreferences();
    }

    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
        String color =sharedPreferences.getString(getString(R.string.pref_color_key),
                getString(R.string.pref_color_celeste_valor));
        if(color.equals(getString(R.string.pref_color_celeste_valor))){
            backgroundColor = ContextCompat.getColor(this, R.color.backgroundBlue);}
        else if (color.equals(getString(R.string.pref_color_rojo_valor)))
            backgroundColor = ContextCompat.getColor(this, R.color.backgroundRed);
        else
            backgroundColor = ContextCompat.getColor(this, R.color.backgroundGreen);
        linearLayout.setBackgroundColor(backgroundColor);
    }
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadColorFromPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_color_key))){
            loadColorFromPreferences(sharedPreferences);
        }
    }
}
