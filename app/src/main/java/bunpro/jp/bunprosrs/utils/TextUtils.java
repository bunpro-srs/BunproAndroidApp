package bunpro.jp.bunprosrs.utils;

import android.os.Build;
import android.text.Html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static String stripHtml(String html) {

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    public static String removeKanji(String str) {

        return str.replaceAll("\\（[^\\（]*?\\）", "");
    }

    public static boolean includeKanji(String str) {
        Pattern p = Pattern.compile("\\（[^\\（]*?\\）", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);

        boolean b = m.find();
        return b;
    }
}
