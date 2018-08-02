package bunpro.jp.bunprosrs.utils;

import android.os.Build;
import android.text.Html;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
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

        return m.find();
    }


    public static List<String> getKanjis(String str) {

        List<String> kanjis = new ArrayList<>();

        Pattern p = Pattern.compile("[\\p{L}]+（.+?）", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);

        while (m.find()) {
            String s = m.group();
            String c = String.valueOf(s.charAt(0));
            while (isJapanese(c)) {
                s = s.substring(1);
                c = String.valueOf(s.charAt(0));
            }

            if (!kanjis.contains(s)) {
                kanjis.add(s);
            }
        }

        return kanjis;
    }

    public static boolean isJapanese(String s) {
        Pattern p = Pattern.compile("[ぁ-ゔゞァ-・ヽヾ゛゜ー]", Pattern.DOTALL);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static String getKana(String s) {
        String s1 = "";
        Pattern p = Pattern.compile("\\（[^\\（]*?\\）", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        if (m.find()) {
            s1 = m.group();
            s1 = s1.replaceAll("（", "");
            s1 = s1.replaceAll("）", "");
        }

        return s1;

    }

    public static String getKanji(String s) {
        return s.replaceAll("\\（[^\\（]*?\\）", "");
    }

}
