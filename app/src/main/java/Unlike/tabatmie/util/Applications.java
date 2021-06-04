package Unlike.tabatmie.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;

import Unlike.tabatmie.R;
import Unlike.tabatmie.database.TabatimeDBHelper;
import butterknife.ButterKnife;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Applications extends Application {

    public static TabatimeDBHelper dbHelper;

    public static Preference preference;

    public static Context context;

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

    public static void fristInit(Activity activity) {
        ButterKnife.bind(activity);
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void logout() {
        try {
            UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                @Override
                public Unit invoke(Throwable throwable) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Applications.preference.pclear();
    }

}
