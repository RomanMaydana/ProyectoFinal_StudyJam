package com.maydana.roman.miepisodio;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class DetalleActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private Toolbar toolbar;
    private ImageView imageDetalleImgView;
    private TextView lugarTxt,fechaTxt,categoriaTxt,descripcionTxt;
    private Episodio episodio;
    private NestedScrollView nestedScrollView;
    private MediaPlayer mp;
    @ColorInt
    private int backgroundColor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Bundle bundle = getIntent().getExtras();
        episodio = (Episodio) bundle.getSerializable(getString(R.string.episodio));
        init();
        setupSharedPreferences();
        llenar();

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }

    private void llenar() {
        File imageFile = new File(episodio.getRutaImagen());
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageDetalleImgView.setImageBitmap(bitmap);
        lugarTxt.setText(episodio.getLugar());
        fechaTxt.setText(episodio.getFecha());
        categoriaTxt.setText(episodio.getCategoria());
        descripcionTxt.setText(episodio.getDescripcion());

    }

    private void init() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(episodio.getTitulo());
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageDetalleImgView = (ImageView)findViewById(R.id.image_detalle_imgView);
        lugarTxt = (TextView)findViewById(R.id.lugar_detalle_txt);
        fechaTxt = (TextView)findViewById(R.id.fecha_detalle_txt);
        categoriaTxt = (TextView)findViewById(R.id.categoria_detalle_txt);
        descripcionTxt = (TextView)findViewById(R.id.descripcion_detalle_txt);
        mp = MediaPlayer.create(this,R.raw.burbuja);
        mp.start();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_color_key))) {
            loadColorFromPreferences(sharedPreferences);
        }
    }

    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
        String color = sharedPreferences.getString(getString(R.string.pref_color_key),
                getString(R.string.pref_color_celeste_valor));
        if (color.equals(getString(R.string.pref_color_celeste_valor))) {
            backgroundColor = ContextCompat.getColor(this, R.color.backgroundBlue);
        } else if (color.equals(getString(R.string.pref_color_rojo_valor)))
            backgroundColor = ContextCompat.getColor(this, R.color.backgroundRed);
        else
            backgroundColor = ContextCompat.getColor(this, R.color.backgroundGreen);
        nestedScrollView.setBackgroundColor(backgroundColor);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadColorFromPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }
}
