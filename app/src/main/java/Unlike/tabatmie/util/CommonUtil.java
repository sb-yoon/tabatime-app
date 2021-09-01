package Unlike.tabatmie.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Insets;
import android.os.Build;
import android.os.PowerManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kakao.sdk.user.UserApiClient;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import Unlike.tabatmie.BuildConfig;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CommonUtil {

    public static final int D_EXERCISE_TIME = 190;
    public static final int D_EXERCISE = 30;
    public static final int D_REST = 10;
    public static final int D_SET = 5;
    public static final int D_ROUND = 1;
    public static final int D_ROUND_RESET = 10;
    public static final boolean D_SOUND = true;
    public static final int D_READY = 5;
    public static final boolean D_PAUSE = true;

    public static final int D_SEC_MAX = 180;
    public static final int D_SEC_MIN = 5;
    public static final int D_COUNT_MAX = 20;
    public static final int D_COUNT_MIN = 1;

    public static String setComma(String number, boolean type, boolean isPlus) {
        try {
            if (number == null || number.equals("")) {
                return "";
            }
            NumberFormat nf = NumberFormat.getInstance();
            String pStr = "";
            if (type) {
                //number = new DecimalFormat("##.#").format(Double.parseDouble(number)).replace(".0","");
                number = new DecimalFormat("##").format(Double.parseDouble(number)).replace(".0", "");
                if (Double.parseDouble(number) > 0 && isPlus) {
                    pStr = "+";
                } else {
                    pStr = "";
                }
                return pStr + nf.format(Double.parseDouble(number));
            } else {
                if (Integer.parseInt(number) > 0 && isPlus) {
                    pStr = "+";
                } else {
                    pStr = "";
                }
                return pStr + nf.format(Integer.parseInt(number));
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return number;
        }
    }

    public static String replaceStr(String title) {
        return title.replaceAll("_", " ");
    }

    public static boolean isPlus(String cash) {
        if (cash == null || cash.equals("")) {
            return false;
        }
        double rCash = 0;
        try {
            rCash = Double.parseDouble(cash);
        } catch (Exception ignore) {

        }
        return rCash > 0;
    }

    public static boolean isEmailValid(CharSequence email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getDateTime(String timeStamp) {
        Date date = new Date(Long.parseLong(timeStamp) * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        return sdf.format(date);
    }

    public static String getDateTimeHistory(String timeStamp) {
        Date date = new Date(Long.parseLong(timeStamp) * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        return sdf.format(date);
    }

    public static long getDateTime(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return Long.parseLong(sdf.format(date));
    }

    public static long newDate() {
        try {
            int newDate = 7;
            Date newday = new Date();
            newday.setTime(new Date().getTime() - ((long) 1000 * 60 * 60 * 24 * newDate));
            return newday.getTime() / 1000;
        } catch (Exception ignore) {
            return 0;
        }
    }

    public static InputFilter spaceFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Pattern ps = Pattern.compile(" ");
            if (ps.matcher(charSequence).matches()) {
                return "";
            }
            return null;
        }
    };

    public static List sortByValue(final HashMap map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v1).compareTo(v2);
            }

        });
        Collections.reverse(list);
        return list;
    }

    public static String addSlashes(String s) {
        s = s.replaceAll("\\\\", "\\\\\\\\");
        s = s.replaceAll("\\n", "\\\\n");
        s = s.replaceAll("\\r", "\\\\r");
        s = s.replaceAll("\\00", "\\\\0");
        s = s.replaceAll("'", "\\\\'");
        return s;
    }

    public static String getVersion(Context context) {
        String version = BuildConfig.VERSION_NAME;
        return version;
    }

    public static int getVersionCode(Context context) {
        int version = BuildConfig.VERSION_CODE;
        return version;
    }

    public static boolean isScreenOnOff(Context context) {
        boolean isOn = false;
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                isOn = pm.isInteractive();
            } else {
                isOn = pm.isScreenOn();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return isOn;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics;
    }

    public static void setMarqueeSpeed(TextView tv, float speed, boolean speedIsMultiplier) {
        try {
            Field f = tv.getClass().getDeclaredField("mMarquee");
            f.setAccessible(true);
            Object marquee = f.get(tv);
            if (marquee != null) {
                String scrollSpeedFieldName = "mScrollUnit";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    scrollSpeedFieldName = "mPixelsPerSecond";
                }

                Field mf = marquee.getClass().getDeclaredField(scrollSpeedFieldName);
                mf.setAccessible(true);
                float newSpeed = speed;
                if (speedIsMultiplier) {
                    newSpeed = mf.getFloat(marquee) * speed;
                }
                mf.setFloat(marquee, newSpeed);
            }
        } catch (Exception e) {
            //Log.i("setMarqueeSpeed", String.format("%s marquee speed set fail fail fail to %f", tv, 0));
            e.printStackTrace();
        }
    }

    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }

    public static boolean checkPermissionPoint(Activity context, String permission) {
        int permissionResult = ContextCompat.checkSelfPermission(context, permission);
        Log.e("cpp : " + permission, "" + permissionResult);
        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                return false;
            }
        }
        return false;
    }

    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getDisplayWidth(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity) context).getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    public static String getTime(int cnt) {
        int hour, min, sec;
        sec = cnt;
        min = sec / 60;
        hour = min / 60;
        sec = sec % 60;
        min = min % 60;
        if (hour > 0) {
            return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
        } else {
            return String.format("%02d", min) + ":" + String.format("%02d", sec);
        }
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
        Applications.preference.put(Preference.TOKEN, "");
        Applications.preference.put(Preference.EMAIL, "");
    }
}
