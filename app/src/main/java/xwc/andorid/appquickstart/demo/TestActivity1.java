package xwc.andorid.appquickstart.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import xwc.andorid.quickstart.ImmersiveActivity;

public class TestActivity1 extends ImmersiveActivity {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_material_design_demo);
        setTitle("AppCompat v7");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_activity_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Goto Navigation Drawer Activity!", Toast.LENGTH_SHORT).show();
        return true;
    }
}
