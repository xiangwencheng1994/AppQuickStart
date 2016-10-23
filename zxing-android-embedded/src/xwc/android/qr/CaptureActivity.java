package xwc.android.qr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.R;

import java.io.IOException;

public class CaptureActivity extends Activity {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean stoped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeScannerView = initializeContent();
        stoped = false;

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        findViewById(R.id.btn_sound).setActivated(capture.getBeepManager().isBeepEnabled());
        capture.decode();
    }

    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.zxing_capture);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeepManager beepManager = capture.getBeepManager();
                boolean state = !capture.getBeepManager().isBeepEnabled();
                beepManager.setBeepEnabled(state);
                v.setActivated(state);
            }
        });
        findViewById(R.id.btn_light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setActivated(barcodeScannerView.toggleTorch());
            }
        });
        findViewById(R.id.btn_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeScannerView.setTorchOff();
                findViewById(R.id.btn_light).setActivated(false);
                v.setEnabled(false);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "选择图标"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(CaptureActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }

    public static final int FILE_SELECT_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            findViewById(R.id.btn_pic).setEnabled(true);
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    new Thread(new DecodeBitmapTask(bitmap)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "文件读取失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class DecodeBitmapTask implements Runnable {
        Bitmap bitmap;

        DecodeBitmapTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            try {
                final Result result = QRCreatorUtil.getResultFromBitmap(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!stoped) {
                            capture.returnResult(new BarcodeResult(result, null));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!stoped)
                            Toast.makeText(CaptureActivity.this, "没有找到任何内容", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        stoped = true;
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
