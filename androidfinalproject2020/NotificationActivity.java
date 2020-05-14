import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {
    public static final String ACTION_UPDATE_NOTIFICATION = "ACTION_UPDATE_NOTIFICATION";
    public static final String PRIMARY_CHANNEL_ID = "CHANNEL ID";
    private static final int NOTIFICATION_ID = 0;

    private Button button_set;
    private Button button_cancel;

    private NotificationManager mNotifyManager;

    private String text_input;
    private TimePicker timePicker;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        setTitle("Go Back");


        createNotificationChannel();

        button_set = findViewById(R.id.setTime);
        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        button_cancel = (Button) findViewById(R.id.cancelTime);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });
    }

    public void createNotificationChannel() {

        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                     "Channel",
                     NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Channel Description");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    public void sendNotification() {

        editText = findViewById(R.id.editText);
        timePicker = findViewById(R.id.reminder);

        text_input = editText.getText().toString();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);

        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
                                                                       0, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        // get time
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar startTime = Calendar.getInstance(); // Picking the time of the reminder
        startTime.set(Calendar.HOUR_OF_DAY, hour);
        startTime.set(Calendar.MINUTE, minute);
        startTime.set(Calendar.SECOND, 0);
        long alarmStartTime = startTime.getTimeInMillis();

        alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, updatePendingIntent);
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

        // update
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }

    public void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);
        Toast.makeText(this, "CANCEL!", Toast.LENGTH_SHORT).show();

    }


    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            getNotificationBuilder();
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, notificationIntent,
                 PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(this, PRIMARY_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Check your App!")
                .setContentText(text_input)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        return notifyBuilder;
    }
}
