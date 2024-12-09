package com.example.lab10;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AudioPlayerService extends Service {
    private static final String CHANNEL_ID = "AudioPlayerServiceChannel";
    private static final int NOTIFICATION_ID = 1; // Identificador único para la notificación
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false; // Indica si el audio está sonando

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mediaPlayer = MediaPlayer.create(this, R.raw.audio_sample);
        mediaPlayer.setLooping(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "START":
                    startPlayback();
                    break;
                case "PAUSE":
                    pausePlayback();
                    break;
                case "RESUME":
                    resumePlayback();
                    break;
                case "SHOW_NOTIFICATION":
                    showNotification();
                    break;
                case "HIDE_NOTIFICATION":
                    hideNotification();
                    break;
                case "STOP":
                    stopPlayback();
                    stopSelf();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Audio Player Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void showNotification() {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("App")
                .setContentText("La app esta en segundo plano")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        startForeground(NOTIFICATION_ID, notification.build());
    }

    private void hideNotification() {
        stopForeground(false); // Detener el servicio en primer plano
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.cancel(NOTIFICATION_ID); // Eliminar la notificación completamente
        }
    }

    private void startPlayback() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            Toast.makeText(this, "Audio iniciado", Toast.LENGTH_SHORT).show();
        }
    }

    private void pausePlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            Toast.makeText(this, "Audio pausado", Toast.LENGTH_SHORT).show();
        }
    }

    private void resumePlayback() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            Toast.makeText(this, "Audio continuado", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            isPlaying = false;
            Toast.makeText(this, "Audio detenido", Toast.LENGTH_SHORT).show();
        }
    }
}
