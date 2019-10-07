package com.example.counter;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ConstraintLayout constraintLayout;
    private TextView counter_increament;
    static int counter = 0;
    private Button Increment_Button , Decrement_Button;
    private TextView small,downn;
    private ImageButton reset_Button;
    private View view;
    private EditText textget;
    private ToggleButton light;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        counter=0;
        reset_Button = (ImageButton)findViewById(R.id.resetid);
        counter_increament = (TextView) findViewById(R.id.textView);
        SharedPreferences score = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        counter = score.getInt("Scoree",0);
        counter_increament.setText(String.valueOf(counter));
        counter_increament.setText("" + String.valueOf(counter));
        Increment_Button = (Button) findViewById(R.id.plusid);
        textget = (EditText)findViewById(R.id.wazifhid);
        Increment_Button.setEnabled(false);
        textget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                {
                    Increment_Button.setEnabled(false);
                }
                else
                {
                    Increment_Button.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Decrement_Button = (Button) findViewById(R.id.mnsid);
        view = this.getWindow().getDecorView();
        constraintLayout = (ConstraintLayout) findViewById(R.id.constrain);
        reset_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Do you want to Reset Counter")
                        .setConfirmText("Reset")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                counter_increament.setText("0");
                                counter =0;
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setCancelText("Close")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismissWithAnimation();

                            }
                        })
                        .show();

            }
        });
        Increment_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Scoree",counter);
                editor.apply();
                counter_increament.setText(String.valueOf(counter));
                try
                {
                    int limit = Integer.parseInt(textget.getText().toString());

                    if(counter==limit)
                    {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("You have Complete your Limit : "+counter)
                                .show();
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.cdd)
                                .setContentTitle("Counter Notifications ")
                                .setContentText("you have complete Counter : "+counter);

                        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);

                        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        builder.setContentIntent(contentIntent);
                        builder.setAutoCancel(true);
                        builder.setLights(Color.BLUE, 500, 500);
                        long[] pattern = {500,500,500,500,500,500,500,500,500};
                        builder.setVibrate(pattern);
                        builder.setStyle(new NotificationCompat.InboxStyle());
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        if(alarmSound == null){
                            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                            if(alarmSound == null){
                                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            }
                        }

                        // Add as notification
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        builder.setSound(alarmSound);
                        manager.notify(1, builder.build());
                    }
                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Use onluy int", Toast.LENGTH_SHORT).show();
                }


            }


        });

        Decrement_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter--;
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Scoree",counter);
                editor.apply();
                counter_increament.setText(String.valueOf(counter));


                if(counter <=0)
                {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("No negative Counting!")
                            .show();
                    counter_increament.setText("0");
                    counter =0;

                }


            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            counter++;
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Scoree", counter);
            editor.apply();
            counter_increament.setText(" " + String.valueOf(counter));
            try {
                int  limit = Integer.parseInt(textget.getText().toString());

                if (counter == limit) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("You have Complete your Limit :"+counter)
                            .show();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.cdd)
                            .setContentTitle("Counter Notifications ")
                            .setContentText("you have complete Counter : " + counter);

                    Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(contentIntent);
                    builder.setAutoCancel(true);
                    builder.setLights(Color.BLUE, 500, 500);
                    long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                    builder.setVibrate(pattern);
                    builder.setStyle(new NotificationCompat.InboxStyle());
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    if (alarmSound == null) {
                        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        if (alarmSound == null) {
                            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        }
                    }

                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    builder.setSound(alarmSound);
                    manager.notify(1, builder.build());
                }

            }catch (Exception e)

            {

            }

            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            --counter;
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Scoree",counter);
            editor.apply();
            counter_increament.setText(" " + String.valueOf(counter));


            if(counter < 0)
            {

                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No negative Counting!")
                        .show();
                counter_increament.setText("0");
                counter =0;

            }

            return true;
        }

        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")








    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.silent) {
            AudioManager audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int currentMode = audiomanage.getRingerMode();
            if (currentMode == AudioManager.RINGER_MODE_SILENT) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Already in Silent Mode !")
                        .show();
            } else {
                audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Silent Mode !")
                        .show();
            }

            } else if (id == R.id.normal) {
            AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            int currentMode = audiomanage.getRingerMode();
            if(currentMode== AudioManager.RINGER_MODE_NORMAL)
            {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Already in Normal Mode !")
                        .show();
            }else {
                audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Normal Mode !")
                        .show();

            }
        } else if (id == R.id.black) {
            constraintLayout.setBackgroundColor(Color.BLACK);

        } else if (id == R.id.blue) {
            constraintLayout.setBackgroundColor(Color.BLUE);

        } else if (id == R.id.skyy) {
            constraintLayout.setBackgroundColor(Color.CYAN);

        } else if (id == R.id.of) {
            final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

            try {
                String camerId = cameraManager.getCameraIdList()[0];

                cameraManager.setTorchMode(camerId, false);




            } catch (Exception a) {

            }


        }
        else if(id==R.id.on)
        {  final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

            try {
                String camerId = cameraManager.getCameraIdList()[0];

                cameraManager.setTorchMode(camerId, true);




            } catch (Exception a) {

            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
