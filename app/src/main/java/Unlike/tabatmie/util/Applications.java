package Unlike.tabatmie.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.kakao.sdk.common.KakaoSdk;

import org.json.JSONArray;

import java.util.ArrayList;

import Unlike.tabatmie.Dto.RecordDTO;
import Unlike.tabatmie.R;
import Unlike.tabatmie.activity.MainActivity;
import Unlike.tabatmie.activity.RecordActivity;
import Unlike.tabatmie.activity.SettingActivity;
import Unlike.tabatmie.activity.StatActivity;
import Unlike.tabatmie.activity.SuccessActivity;
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
                } else if (refreshActivity instanceof SuccessActivity) {
                    ((SuccessActivity) refreshActivity).refresh();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tryAgain() {
        try {
            if (refreshActivity != null) {
                if (refreshActivity instanceof SuccessActivity) {
                    ((SuccessActivity) refreshActivity).tryAgain();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStatistics(int lank, int cnt, int time) {
        try {
            if (refreshActivity != null) {
                if (refreshActivity instanceof StatActivity) {
                    ((StatActivity) refreshActivity).setStat(lank, cnt, time);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRecord(ArrayList<RecordDTO> recordList) {
        try {
            if (refreshActivity != null) {
                if (refreshActivity instanceof RecordActivity) {
                    ((RecordActivity) refreshActivity).setRecord(recordList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
