package Unlike.tabatmie.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.kakao.sdk.common.KakaoSdk;

import Unlike.tabatmie.R;
import Unlike.tabatmie.activity.MainActivity;
import Unlike.tabatmie.activity.SettingActivity;
import Unlike.tabatmie.database.TabatimeDBHelper;


public class Applications extends Application {

    public static TabatimeDBHelper dbHelper;

    public static Preference preference;

    public static Context context;
    public static Activity refreshActivity;

    public Applications() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        dbHelper = new TabatimeDBHelper(getApplicationContext());
        dbHelper.open();
        preference = new Preference(getApplicationContext());

        KakaoSdk.init(this, this.getResources().getString(R.string.kakao_app_key));
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setRefreshActivity(Activity refreshActivity) throws Exception {
        Applications.refreshActivity = refreshActivity;
    }

    public static void refreshActivity() {
        try {
            if (refreshActivity != null) {
                if (refreshActivity instanceof MainActivity) {
                    ((MainActivity) refreshActivity).refresh();
                } else if (refreshActivity instanceof SettingActivity) {
                    ((SettingActivity) refreshActivity).refresh();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
