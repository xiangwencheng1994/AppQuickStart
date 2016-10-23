package xwc.andorid.appquickstart.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xwc.andorid.quickstart.ImmersiveActivity;
import xwc.andorid.quickstart.SkinApplication;

public class SkinSettingActivity extends ImmersiveActivity {
    private RecyclerView recyclerView;

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
        setContentView(R.layout.activity_skin_setting);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initViews();
        setTitle("Sample Skin Demo");
    }

    private void initViews(){
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<StyleSkin> skins = new ArrayList<>();
        skins.add(new StyleSkin("Default", R.style.AppTheme_Activity, R.color.Default));
        skins.add(new StyleSkin("Teal", R.style.AppTheme_Activity_Teal, R.color.Teal));
        skins.add(new StyleSkin("Blue", R.style.AppTheme_Activity_Blue, R.color.Blue));
        skins.add(new StyleSkin("Brown", R.style.AppTheme_Activity_Brown, R.color.Brown));
        skins.add(new StyleSkin("Orange", R.style.AppTheme_Activity_Orange, R.color.Orange));
        SkinListAdapter adapter = new SkinListAdapter(this, skins);
        recyclerView.setAdapter(adapter);
    }

    public class StyleSkin{
        public String name;
        public @StyleRes int style;
        public @DrawableRes int preview;
        public boolean used;
        public StyleSkin(String name, @StyleRes int style, @DrawableRes int preview){
            this.name = name;
            this.style = style;
            this.preview = preview;
            if (style == ((SkinApplication)getApplication()).getStyle()) used = true;
        }
    }

    private View.OnClickListener onSkinClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StyleSkin skin = (StyleSkin) v.getTag();
            if (skin.used) return;
            ((SkinApplication)getApplication()).setTheme(skin.style);
        }
    };

    public class SkinListAdapter extends RecyclerView.Adapter<SkinListAdapter.SkinPreViewHolder>{
        private List<StyleSkin> skins;
        private LayoutInflater inflater;

        public SkinListAdapter(Context context, List<StyleSkin>skins){
            this.skins = skins;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public SkinPreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_skin_pre,parent, false);
            view.setOnClickListener(onSkinClick);
            SkinPreViewHolder holder = new SkinPreViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(SkinPreViewHolder holder, int position) {
            holder.ShowSkin(skins.get(position));
        }
        @Override
        public int getItemCount() {
            return skins.size();
        }

        class SkinPreViewHolder extends RecyclerView.ViewHolder{
            private TextView name, used;
            public SkinPreViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.skin_name);
                used = (TextView) itemView.findViewById(R.id.skin_used);
            }
            public void ShowSkin(StyleSkin skin){
                name.setText(skin.name);
                this.itemView.setBackgroundResource(skin.preview);
                used.setVisibility(skin.used? View.VISIBLE : View.GONE);
                itemView.setTag(skin);
            }
        }
    };
}
