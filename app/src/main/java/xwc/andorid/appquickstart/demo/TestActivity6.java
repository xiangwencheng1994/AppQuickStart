package xwc.andorid.appquickstart.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;

import xwc.andorid.appquickstart.demo.view.IncreaseView;
import xwc.andorid.quickstart.ImmersiveActivity;

public class TestActivity6 extends ImmersiveActivity implements View.OnTouchListener {

    private MP3Recorder mp3Recorder;
    private Handler handler;
    private IncreaseView increaseView;
    private TextView text_db;

    private Runnable updateImage = new Runnable() {
        @Override
        public void run() {
            increaseView.setProgress(mp3Recorder.getVolume());
            text_db.setText(mp3Recorder.getdB() + "dB");
            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected Toolbar onCreateToolbar(@NonNull Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.toolbar_icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiorecorder);
        increaseView = (IncreaseView) findViewById(R.id.increaseView);
        text_db = (TextView) findViewById(R.id.text_db);
        handler = new Handler();
        findViewById(R.id.btn_start).setOnTouchListener(this);
        setTitle("AudioRecorder");
        mp3Recorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(), "temp.mp3"));
        increaseView.setMax(mp3Recorder.getMaxVolume());
        increaseView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("About Lame Mp3 Encoder");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("libmp3lame: v3.99.5")
                .setMessage("Copy right with LGPL\nmore detail @link http://lame.sourceforge.net")
                .show();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    ((Button)v).setText("Recording");
                    mp3Recorder.start();
                    increaseView.setVisibility(View.VISIBLE);
                    text_db.setVisibility(View.VISIBLE);
                    handler.postDelayed(updateImage, 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                mp3Recorder.stop();
                increaseView.setVisibility(View.INVISIBLE);
                text_db.setVisibility(View.INVISIBLE);
                handler.removeCallbacks(updateImage);
                ((Button)v).setText("按住说话");
                Toast.makeText(this, "文件已保存至:" + Environment.getExternalStorageDirectory().getPath() + "/temp.mp3", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mp3Recorder != null) mp3Recorder.stop();
        super.onDestroy();
    }
}
