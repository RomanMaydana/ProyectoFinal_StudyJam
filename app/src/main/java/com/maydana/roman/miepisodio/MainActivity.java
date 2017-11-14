package com.maydana.roman.miepisodio;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.maydana.roman.miepisodio.data.SQLiteHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class MainActivity extends AppCompatActivity implements EpisodioAdapter.onEpisodioSelectListener, SharedPreferences.OnSharedPreferenceChangeListener {
    public static SQLiteHelper sqLiteHelper;
    private RecyclerView mRecycleView;
    public static EpisodioAdapter episodioAdapter;
    private List<Episodio> list;


    @ColorInt
    private int backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycleView = (RecyclerView) findViewById(R.id.recycle_main);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setItemAnimator(new FadeInLeftAnimator());
        mRecycleView.setHasFixedSize(true);

        setupSharedPreferences();


        list = new ArrayList<>();
        episodioAdapter = new EpisodioAdapter(this, this);

        ((EpisodioAdapter) episodioAdapter).setMode(Attributes.Mode.Single);
        sqLiteHelper = new SQLiteHelper(this, getString(R.string.nombre_baseDatos), null, 1);
        sqLiteHelper.queryData(getString(R.string.crear_tabla));
        try {
            Cursor cursor = sqLiteHelper.getData(getString(R.string.selec_base_datos));
            list.clear();
            llenarDatos(cursor);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mRecycleView.setAdapter(episodioAdapter);
        mRecycleView.setOnScrollListener(onScrollListener);

    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadColorFromPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

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
        mRecycleView.setBackgroundColor(backgroundColor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void llenarDatos(Cursor cursor) {
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String titulo = cursor.getString(1);
            String lugar = cursor.getString(2);
            String fecha = cursor.getString(3);
            String categoria = cursor.getString(4);
            String descripcion = cursor.getString(5);
            String imagen = cursor.getString(6);
            list.add(new Episodio(id, titulo, lugar, fecha, categoria, descripcion, imagen));
        }
        episodioAdapter.setData(list);
    }

    public void adicionarEpisodio(View view) {
        Intent intent = new Intent(MainActivity.this, AddEpisodioActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEpisodioSelected(Episodio episodio) {

        Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
        intent.putExtra(getString(R.string.episodio), episodio);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onEpisodioLongSelected(Episodio episodio, int pos) {
        showDialogUpdate(MainActivity.this, episodio, pos);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.acerca_de) {
            Intent intent = new Intent(MainActivity.this, AcercaDeActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private static final String TAG = "AddEpisodioActivity";
    private ImageView fotoAdd;
    private EditText tituloAdd, descripcionAdd, lugarAdd;
    private TextView fechaAdd;
    private Spinner spinner;
    private String categoria = "";
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Dialog dialog;
    private Button actButton;
    private String rutaImagen;
    private LinearLayout linearLayoutUpdate;


    public void showDialogUpdate(Activity activity, final Episodio episodio, final int pos) {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_episodio_activity);
        dialog.setTitle(getResources().getString(R.string.actualizar));
        init();
        linearLayoutUpdate.setBackgroundColor(backgroundColor);
        int with = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.98);
        int heigth = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.98);
        dialog.getWindow().setLayout(with, heigth);
        dialog.show();

        tituloAdd.setText(episodio.getTitulo());
        lugarAdd.setText(episodio.getLugar());
        fechaAdd.setText(episodio.getFecha());
        categoria = episodio.getCategoria();
        descripcionAdd.setText(episodio.getDescripcion());
        rutaImagen = episodio.getRutaImagen();
        File imageFile = new File(episodio.getRutaImagen());
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        fotoAdd.setImageBitmap(bitmap);

        actButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tituloAdd.getText().toString().compareTo("") == 0 || fechaAdd.getText().toString() == getString(R.string.seleccione_fecha) || lugarAdd.getText().toString().compareTo("") == 0 || descripcionAdd.getText().toString().compareTo("") == 0) {
                    Toast.makeText(MainActivity.this, R.string.complete_todos_los_campos, Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.sqLiteHelper.updateData(tituloAdd.getText().toString().trim(), lugarAdd.getText().toString().trim(), fechaAdd.getText().toString().trim(), categoria.trim(), descripcionAdd.getText().toString().trim(), episodio.getRutaImagen(), episodio.getId());
                episodioAdapter.setItemData(new Episodio(episodio.getId(), tituloAdd.getText().toString(), lugarAdd.getText().toString(), fechaAdd.getText().toString(), categoria, descripcionAdd.getText().toString().trim(), episodio.getRutaImagen()), pos);
                dialog.dismiss();

                Toast.makeText(MainActivity.this, R.string.actualizado_exitosamente, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void init() {
        linearLayoutUpdate = dialog.findViewById(R.id.linearLayoutUpdate);
        fotoAdd = dialog.findViewById(R.id.fotoAdd_image_button);
        tituloAdd = dialog.findViewById(R.id.tituloAdd_edit_Text);
        lugarAdd = dialog.findViewById(R.id.lugarAdd_edit_Text);
        fechaAdd = dialog.findViewById(R.id.fechaAdd_text_view);
        descripcionAdd = dialog.findViewById(R.id.descripionAdd_edit_Text);
        spinner = dialog.findViewById(R.id.spinerOption);
        actButton = dialog.findViewById(R.id.actualizar_button);
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
        DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener, año, mes, dia);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}



