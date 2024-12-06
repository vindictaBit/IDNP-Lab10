package com.example.lab10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicita el permiso si no está otorgado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    public void onStartClick(View view) {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.setAction("START");
        startService(intent);
    }

    public void onPauseClick(View view) {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.setAction("PAUSE");
        startService(intent);
    }

    public void onResumeClick(View view) {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.setAction("RESUME");
        startService(intent);
    }

    public void onStopClick(View view) {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.setAction("STOP");
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Mostrar notificación al entrar en segundo plano
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.setAction("SHOW_NOTIFICATION");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ocultar notificación al volver a primer plano
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.setAction("HIDE_NOTIFICATION");
        startService(intent);
    }
}
