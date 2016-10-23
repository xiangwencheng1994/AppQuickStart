package xwc.andorid.appquickstart.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import xwc.andorid.appquickstart.demo.view.VisualizerFFTView;
import xwc.andorid.appquickstart.demo.view.VisualizerView;
import xwc.andorid.quickstart.ImmersiveActivity;

public class TestActivity7 extends ImmersiveActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VisualizerView visualizerView;
    private VisualizerFFTView visualizerFFTView;
    private Visualizer mVisualizer;

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
        setContentView(R.layout.activity_player_fft);
        visualizerView = (VisualizerView) findViewById(R.id.visualizerView);
        visualizerFFTView = (VisualizerFFTView) findViewById(R.id.visualizerFFTView);
        findViewById(R.id.btn_start).setOnClickListener(this);
        setTitle("MediaPlayer with Visualizer");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        visualizerFFTView.init();
    }

    public static final int FILE_SELECT_CODE = 1;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start){
            if (mVisualizer != null){
                mVisualizer.release();
                mVisualizer = null;
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult( Intent.createChooser(intent, "选择一个音频文件"), FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Visualizer.OnDataCaptureListener onDataCaptureListener = new Visualizer.OnDataCaptureListener() {
        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
            visualizerView.updateVisualizer(waveform);
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
            visualizerFFTView.updateVisualizer(fft);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FILE_SELECT_CODE){
            if (resultCode != Activity.RESULT_OK){
                Toast.makeText(this, "你取消了选择", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            int maxCR = Visualizer.getMaxCaptureRate();
            int aId = mediaPlayer.getAudioSessionId();
            mVisualizer = new Visualizer(aId);
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            mVisualizer.setDataCaptureListener(onDataCaptureListener, maxCR / 2, true, true);
            mVisualizer.setEnabled(true);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mVisualizer != null){
            mVisualizer.release();
            mVisualizer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

}
