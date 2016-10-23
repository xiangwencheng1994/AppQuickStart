package xwc.andorid.appquickstart.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import xwc.android.qr.CaptureActivity;

import java.io.File;
import java.io.IOException;

import xwc.andorid.quickstart.ImmersiveActivity;
import xwc.android.qr.QRCreatorUtil;

public class TestActivity8 extends ImmersiveActivity implements View.OnClickListener {

    private Bitmap bitmap;
    private TextView filePath;
    private EditText editText;
    private ImageView qr;
    private File qr_temp;

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
        setContentView(R.layout.activity_qr_demo);
        filePath = (TextView) findViewById(R.id.text_icon);
        editText = (EditText) findViewById(R.id.editText);
        qr = (ImageView) findViewById(R.id.src_qr);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        setTitle("QR code demo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_activity_8, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                new IntentIntegrator(this).initiateScan(IntentIntegrator.QR_CODE_TYPES);
                break;
            case R.id.action_about:
                new AlertDialog.Builder(this)
                        .setTitle("zxing:core: 3.3.0")
                        .setMessage("Copy right with  Apache License 2.0\nmore detail @link https://github.com/zxing/zxing")
                        .show();
                break;
        }
        return true;
    }

    public static final int FILE_SELECT_CODE = 1;
    public static final int ACTIVITY_SCAN_CODE = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "选择图标"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_start:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text = editText.getText().toString().trim();
                        int w = qr.getWidth(), h = qr.getHeight();
                        int qr_pix = w > h ? h : w;
                        try {
                            qr_temp = File.createTempFile("qr_temp", "bmp");
                            if (QRCreatorUtil.createQRImage(text, qr_pix, qr_pix, bitmap, qr_temp)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        qr.setImageBitmap(BitmapFactory.decodeFile(qr_temp.getPath()));
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                bitmap = null;
                filePath.setText(R.string.selectIcon);
                Toast.makeText(this, "你取消了图标", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap == null) {
                    filePath.setText(R.string.selectIcon);
                    Toast.makeText(this, "图片解析失败", Toast.LENGTH_LONG).show();
                } else {
                    filePath.setText(uri.getPath());
                }
            }
        }
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "你取消了扫描", Toast.LENGTH_SHORT).show();
            } else {
                String result = data.getExtras().getString(Intents.Scan.RESULT);
                new AlertDialog.Builder(this)
                        .setTitle("扫描结果")
                        .setMessage(result)
                        .show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
