package xwc.andorid.appquickstart.demo;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import xwc.andorid.quickstart.ImmersiveActivity;

public class TestActivity4 extends ImmersiveActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

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
        setContentView(R.layout.activity_top_tab);

        if(mSectionsPagerAdapter == null) mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setTitle("Top TabLayout Demo");
    }


    public static class PlaceholderFragment extends Fragment implements DownloadListener {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static Map<Integer, View> views = new HashMap<>();
        public static final String titles[] = {"百度", "腾讯", "微博"};
        public static final String urls[] = {"http://www.baidu.com", "http://www.qq.com", "http://weibo.com"};
        public int pos;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public WebViewClient webViewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            pos = getArguments().getInt(ARG_SECTION_NUMBER);
            View view = views.get(pos);
            if (view != null) return view;
            WebView webView = new WebView(getContext());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setSupportZoom(true);
            webView.loadUrl(urls[pos]);
            webView.setWebViewClient(webViewClient);
            webView.setDownloadListener(this);
            views.put(pos, webView);
            return webView;
        }

        @Override
        public void onDestroy() {
            WebView view = (WebView) views.get(pos);
            view.removeAllViews();
            view.destroy();
            views.remove(pos);
            System.gc();
            super.onDestroy();
        }

        /**
         * Notify the host application that a file should be downloaded
         *
         * @param url                The full url to the content that should be downloaded
         * @param userAgent          the user agent to be used for the download.
         * @param contentDisposition Content-disposition http header, if
         *                           present.
         * @param mimetype           The mimetype of the content reported by the server
         * @param contentLength      The file size reported by the server
         */
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.i("tag", "url="+url);
            Log.i("tag", "userAgent="+userAgent);
            Log.i("tag", "contentDisposition="+contentDisposition);
            Log.i("tag", "mimetype="+mimetype);
            Log.i("tag", "contentLength="+contentLength);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS, "my_down");
            DownloadManager downManager = (DownloadManager)getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            downManager.enqueue(request);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        private Fragment[] fragments = new Fragment[3];
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragments[position];
            if (fragment == null) {
                fragment = PlaceholderFragment.newInstance(position);
                fragments[position] = fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PlaceholderFragment.titles[position];
        }
    }
}
