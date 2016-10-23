package xwc.andorid.appquickstart.demo;

import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xwc.andorid.quickstart.ImmersiveActivity;

public class TestActivity3 extends ImmersiveActivity implements TabLayout.OnTabSelectedListener {
    private Map<Integer, Fragment> fragmentMap = new HashMap<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        initView();
        setTitle("Bottom TabLayout");
    }

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

    void initView() {
        if (adapter == null) {
            adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private String[] mTitles = new String[]{"微信", "通讯录", "发现", "我"};

                @Override
                public Fragment getItem(int position) {
                    Fragment fragment = fragmentMap.get(position);
                    if (fragment == null) {
                        fragment = new TestFragment();
                        ((TestFragment) fragment).setMsg("Frame " + position);
                        fragmentMap.put(position, fragment);
                    }
                    return fragment;
                }

                @Override
                public int getCount() {
                    return mTitles.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mTitles[position];
                }
            };
        }
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setTag(new TabIcon(R.mipmap.tabbar_mainframe, R.mipmap.tabbar_mainframe_active));
        tabLayout.getTabAt(1).setTag(new TabIcon(R.mipmap.tabbar_contacts, R.mipmap.tabbar_contacts_active));
        tabLayout.getTabAt(2).setTag(new TabIcon(R.mipmap.tabbar_discover, R.mipmap.tabbar_discover_active));
        tabLayout.getTabAt(3).setTag(new TabIcon(R.mipmap.tabbar_me, R.mipmap.tabbar_me_active));

        tabLayout.addOnTabSelectedListener(this);
        refreshTabs();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        refreshTabs();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public void refreshTabs() {
        int count = tabLayout.getTabCount();
        int cur = tabLayout.getSelectedTabPosition();
        for (int i = 0; i < count; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            TabIcon tabIcon = (TabIcon) tab.getTag();
            if (i == cur) {
                Drawable drawable = getResources().getDrawable(tabIcon.selectedIcon);
                TypedArray array = getTheme().obtainStyledAttributes(new int[]{
                        android.R.attr.colorPrimary});
                int color = array.getColor(0, ContextCompat.getColor(this, R.color.colorPrimary));
                array.recycle();
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                tab.setIcon(drawable);
            } else {
                tab.setIcon(tabIcon.unselectedIcon);
            }
        }
        tabLayout.invalidate();
    }

    public class TabIcon {
        public
        @DrawableRes
        int selectedIcon, unselectedIcon;

        public TabIcon(@DrawableRes int unselectedIcon, @DrawableRes int selectedIcon) {
            this.unselectedIcon = unselectedIcon;
            this.selectedIcon = selectedIcon;
        }
    }

    public static class TestFragment extends Fragment {
        private String msg;
        private TextView tv;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            tv = new TextView(getContext());
            tv.setId(R.id.textView);
            tv.setText(msg);
            tv.setTextSize(24);
            return tv;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("msg", msg);
        }

        @Override
        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);
            if (savedInstanceState != null) {
                msg = savedInstanceState.getString("msg");
                tv.setText(msg);
            }
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
