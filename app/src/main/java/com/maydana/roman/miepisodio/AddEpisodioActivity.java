package com.maydana.roman.miepisodio;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddEpisodioActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "AddEpisodioActivity";
    private LinearLayout linearLayout;
    private ImageButton fotoAdd;
    private EditText tituloAdd, descripcionAdd, lugarAdd;
    private TextView fechaAdd;
    private Spinner spinner;
    final int REQUEST_CODE_GALERY = 1;
    private String categoria = "";
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Bitmap bitmap = null;

    @ColorInt
    private int backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episodio);

        init();
        setupSharedPreferences();
    }

    private void init() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        fotoAdd = (ImageButton) findViewById(R.id.fotoAdd_image_button);
        tituloAdd = (EditText) findViewById(R.id.tituloAdd_edit_Text);
        lugarAdd = (EditText) findViewById(R.id.lugarAdd_edit_Text);
        fechaAdd = (TextView) findViewById(R.id.fechaAdd_text_view);
        descripcionAdd = (EditText) findViewById(R.id.descripionAdd_edit_Text);
        spinner = (Spinner) findViewById(R.id.spinerOption);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Select_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoria = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
                mes = mes + 1;
                Log.d(TAG, getString(R.string.enFecha) + mes + "/" + dia + "/" + año);
                String fecha = mes + "/" + dia + "/" + año;
                fechaAdd.setText(fecha);
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void seleccionFecha(View view) {
        Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(AddEpisodioActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener, año, mes, dia);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_adicionar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_adicionar) {

            try {
                if (tituloAdd.getText().toString().compareTo("") == 0 || fechaAdd.getText().toString() == getString(R.string.seleccione_fecha) || categoria == "" || bitmap == null || lugarAdd.getText().toString().compareTo("") == 0 || descripcionAdd.getText().toString().compareTo("") == 0) {
                    Toast.makeText(this, R.string.complete_todos_los_campos, Toast.LENGTH_SHORT).show();
                    return false;
                }
                Guardar guardar = new Guardar();
                guardar.SaveImage(this, bitmap);
                String rutaImagen = guardar.getUriPath();
                MainActivity.sqLiteHelper.insertData(tituloAdd.getText().toString().trim(), lugarAdd.getText().toString().trim(), fechaAdd.getText().toString().trim(), categoria.trim(), descripcionAdd.getText().toString().trim(), rutaImagen);
                MainActivity.episodioAdapter.addItemData(new Episodio(0, tituloAdd.getText().toString().trim(), lugarAdd.getText().toString().trim(), fechaAdd.getText().toString().trim(), categoria.trim(), descripcionAdd.getText().toString().trim(), rutaImagen));
                Toast.makeText(this, R.string.agragado_exitosamente, Toast.LENGTH_SHORT).show();
                tituloAdd.getText().clear();
                lugarAdd.getText().clear();
                fechaAdd.setText(getString(R.string.seleccione_fecha));
                bitmap = null;
                descripcionAdd.getText().clear();
                fotoAdd.setImageResource(R.drawable.ic_image_black_24dp);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertarImagen(View view) {
        ActivityCompat.requestPermissions(AddEpisodioActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(getString(R.string.image));
                startActivityForResult(intent, REQUEST_CODE_GALERY);
            } else {
                Toast.makeText(this, R.string.no_tiene_permiso, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                fotoAdd.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
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
        linearLayout.setBackgroundColor(backgroundColor);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadColorFromPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

}
