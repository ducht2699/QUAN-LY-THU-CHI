package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {
    private TextView txtTitle;
    private ImageView img_intro;
    private Animation animationText, animationImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        txtTitle = findViewById(R.id.tvTitleIntro);
        img_intro = findViewById(R.id.img_intro);
        animationText = AnimationUtils.loadAnimation(this, R.anim.text_intro_animation);
        txtTitle.setAnimation(animationText);
        animationImage = AnimationUtils.loadAnimation(this, R.anim.image_intro_animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
