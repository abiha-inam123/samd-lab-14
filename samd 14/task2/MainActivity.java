package com.example.samd_lab_14_task_2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager)
                        getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkCapabilities networkCapabilities =
                        cm.getNetworkCapabilities(cm.getActiveNetwork());
                int downloadSpeed =
                        networkCapabilities.getLinkDownstreamBandwidthKbps();
                int uploadSpeed = networkCapabilities.getLinkUpstreamBandwidthKbps();
                downloadSpeed = downloadSpeed / 1000;
                uploadSpeed = uploadSpeed / 1000;

                String text = "Upload Speed: " + uploadSpeed + " mbps\nDownload Speed:" +
                downloadSpeed + " mbps";
                addNotification("Internet Speed", text);
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNotification(String title, String text) {
        NotificationManager notificationManager;
        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(getApplicationContext(), "notify_001");
        NotificationCompat.BigTextStyle bigText = new
                NotificationCompat.BigTextStyle();
        bigText.bigText(text);
        bigText.setBigContentTitle(title);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        notificationManager = (NotificationManager)
                getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "";
            NotificationChannel channel = new NotificationChannel(
                    id, "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(id);
        }
        notificationManager.notify(0, mBuilder.build());
    }
}
