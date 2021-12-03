package com.example.app_mulltimedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button btnani;
    ImageView icono;

    ObjectAnimator animationRotation;

    AnimatorSet animatorSet;

    long animationDuration = 1000;

    MediaRecorder grabacion;
    String archivoSalida = null;
    Button btnRecorder, play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animatorSet = new AnimatorSet();

        btnani = findViewById(R.id.btnAnim);
        icono = findViewById(R.id.icon);
        btnani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animacion();
            }
        });

        LinearLayout layin = findViewById(R.id.layin);
        VideoView video = new VideoView(this);
        String origen = "android.resource://" + getPackageName() +"/" + R.raw.video;
        Uri uri = Uri.parse(origen);
        video.setVideoURI(uri);
        //video.start();
        LinearLayout contenedor = new LinearLayout(this);
        MediaController controlador = new MediaController(this);
        video.setMediaController(controlador);
        controlador.setAnchorView(video);
        contenedor.addView(video);
        layin.addView(contenedor);

        btnRecorder = findViewById(R.id.btnRec);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }

    }

    public void animacion () {
        animationRotation = ObjectAnimator.ofFloat(icono, "rotation",0f, 360f);
        animationRotation.setDuration(animationDuration);
        AnimatorSet animatorSetRotator = new AnimatorSet();
        animatorSetRotator.playTogether(animationRotation);
        animatorSetRotator.start();
    }


    public void Recorder (View view) {
        if(grabacion == null) {
            archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Grabacion.mp3";
            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            grabacion.setOutputFile(archivoSalida);

            try {
                grabacion.prepare();
                grabacion.start();
            }catch (IOException e){     }

            btnRecorder.setBackgroundResource(R.drawable.ic_baseline_fiber_manual_record_24);
            Toast.makeText(getApplicationContext(), "Grabando...", Toast.LENGTH_SHORT).show();
        }
        else if(grabacion!= null){
            grabacion.stop();
            grabacion.release();
            grabacion = null;
            btnRecorder.setBackgroundResource(R.drawable.stop);
            Toast.makeText(getApplicationContext(), "Grabación Finalizada", Toast.LENGTH_SHORT).show();
        }
    }

    public void reproducir(View view) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(archivoSalida);
            mediaPlayer.prepare();
        } catch (IOException e) {
        }
        Toast.makeText(getApplicationContext(), "Reproduciendo Audio...", Toast.LENGTH_SHORT).show();
    }
}