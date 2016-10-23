package xwc.andorid.appquickstart.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import java.io.IOException;

import xwc.andorid.quickstart.ImmersiveActivity;
import xwc.andorid.quickstart.net.OkHttpClientManager;

public class TestActivity5 extends ImmersiveActivity {

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

    private EditText url;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttpclientmanager);
        url = (EditText) findViewById(R.id.edit_url);
        textView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClientManager.getAsyn(url.getText().toString(), new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        textView.setText(e.toString());
                    }
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }
                });
            }
        });
        try {
            OkHttpClientManager.displayImage((ImageView) findViewById(R.id.imageView), "http://ww2.sinaimg.cn/bmiddle/b99e8bc3jw1enjadjka8yj20fa0a6mxn.jpg", R.mipmap.ic_launcher);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("OkHttpClientManager");
    }

}
