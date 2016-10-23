package xwc.andorid.quickstart;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.support.annotation.StyleRes;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple skin setting application.
 * You application class should be subclass of this.
 */

public class SkinApplication extends Application {
    private @StyleRes int style;

    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        activities = new ArrayList<>();
        setTheme(R.style.AppTheme_Activity);
    }

    /**
     * set Theme for app, and all running activities will be recreated.
     * this method mast be called by UI thread.
     * @param style StyleRes declare in xml Resource.
     */
    @Override
    public void setTheme(@StyleRes int style) {
        super.setTheme(style);
        this.style = style;
        int l = activities.size() - 1;
        while (l >= 0){
            Activity activity = activities.get(l);
            activity.recreate();
            l--;
        }
    }

    /**
     * get Current app style
     * @return Current StyleRes declare in xml Resource.
     */
    public @StyleRes int getStyle(){return style;}

    /**
     * Activity to bind self to application.
     * SkinActivity will auto call this method when onCreate.
     * All need support skin activity should call this method.
     * we suggest you make all activity be subclass of SkinActivity.
     * @param activity
     */
    public void addActivity(Activity activity){
        this.activities.add(activity);
    }

    /**
     * Activity to unbind self from application.
     * SkinActivity will be call this method when onDestroy.
     * All need support skin activity should call this method,
     * we suggest you make all activity be subclass of SkinActivity.
     * @param activity
     */
    public void removeActivity(Activity activity){
        this.activities.remove(activity);
    }

    /**
     * Get binding activities.
     * @return the list of activities order by bind time.
     */
    public List<Activity> getActivities(){return activities;}
}
