package xwc.andorid.appquickstart.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xwc.andorid.quickstart.ImmersiveActivity;

public class MainActivity extends ImmersiveActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);
        setContentView(listView);

        setTitle("AppStart");
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getListItems()));

        listView.setOnItemClickListener(this);
    }

    private ArrayList<ActivityItem> menuMap;

    private List<String> getListItems(){
        menuMap = new ArrayList<>();
        menuMap.add(new ActivityItem("Material Design", TestActivity1.class));
        menuMap.add(new ActivityItem("Navigation Drawer Activity", TestActivity2.class));
        menuMap.add(new ActivityItem("Bottom TabLayout Activity", TestActivity3.class));
        menuMap.add(new ActivityItem("Top TabLayout Activity", TestActivity4.class));
        menuMap.add(new ActivityItem("Network OkHttpClientManager", TestActivity5.class));
        menuMap.add(new ActivityItem("AudioRecorder with Lame Mp3", TestActivity6.class));
        menuMap.add(new ActivityItem("MediaPlayer with Visualizer", TestActivity7.class));
        menuMap.add(new ActivityItem("QR code with zxing", TestActivity8.class));
        return ActivityItem.getTitles(menuMap);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class clz = menuMap.get(position).cls;
        if (clz == null) return;
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public static class ActivityItem{
        public String title;
        public Class cls;
        public ActivityItem(String title, Class cls){this.title = title; this.cls = cls;}
        public static List<String> getTitles(List<ActivityItem> datas){
            ArrayList<String> titles = new ArrayList<>();
            for (ActivityItem item : datas) titles.add(item.title);
            return titles;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_skin){
            Intent intent = new Intent(this, SkinSettingActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
