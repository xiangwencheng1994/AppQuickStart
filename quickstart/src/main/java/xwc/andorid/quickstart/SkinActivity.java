package xwc.andorid.quickstart;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;

/**
 * Simple SkinActivity extends AppCompatActivity.
 * all activities subclass of this will auto bind in SkinApplication.
 */

public class SkinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Application app = getApplication();
        if (app instanceof SkinApplication){
            setTheme(((SkinApplication) app).getStyle());
            ((SkinApplication) app).addActivity(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Application app = getApplication();
        if (app instanceof SkinApplication){
            ((SkinApplication) app).removeActivity(this);
        }
        super.onDestroy();
    }
}
