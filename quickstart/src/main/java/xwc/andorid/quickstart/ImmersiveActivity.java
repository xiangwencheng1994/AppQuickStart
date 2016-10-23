package xwc.andorid.quickstart;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A universal activity use layout@R.layout.activity_immersive_base to initialize the activity.
 * you can override the layout@R.layout.activity_immersive_base to set up specified the activity,but
 * must contains a android.support.v7.widget.Toolbar@R.id.toolbar and a FrameLayout@R.id.contentLayout
 *
 */

public class ImmersiveActivity extends SkinActivity {

    /**
     * initialize the toolbar，Override this method to setting specified toolbar
     * @param toolbar default toolbar
     * @return initialized toolbar if return null, will be remove toolbar.
     */
    protected Toolbar onCreateToolbar(@NonNull Toolbar toolbar){
        return toolbar;
    }

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_immersive_base, null);
        ViewGroup contentLayout = (ViewGroup) root.findViewById(R.id.contentLayout);
        View contentView = inflater.inflate(layoutResID, null);
        contentLayout.addView(contentView);
        super.setContentView(root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar = onCreateToolbar(toolbar);
        if (toolbar == null){
            setSupportActionBar(null);
            super.setContentView(layoutResID);
        }
    }

    @Override
    public final void setContentView(View contentView) {
        ViewGroup root = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_immersive_base, null);
        ViewGroup contentLayout = (ViewGroup) root.findViewById(R.id.contentLayout);
        contentLayout.addView(contentView);
        super.setContentView(root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar = onCreateToolbar(toolbar);
        if (toolbar == null){
            setSupportActionBar(null);
            super.setContentView(contentView);
        }
    }

}
