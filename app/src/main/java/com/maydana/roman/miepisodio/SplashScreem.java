package com.maydana.roman.miepisodio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreem extends AppCompatActivity {
    private ImageView logoImagenView,tituloImagenView;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screem);
        logoImagenView = (ImageView)findViewById(R.id.logoImagenView);
        tituloImagenView = (ImageView)findViewById(R.id.tituloImagenView);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splash_screem);
        logoImagenView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Animation animacion = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                tituloImagenView.startAnimation(animacion);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
