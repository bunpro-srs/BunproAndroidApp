package bunpro.jp.bunproapp.utils;

public class EnvVarLoader {
    static public String getEnvVar(String key) {
        if (System.getenv().containsKey(key)) {
            return System.getenv().get(key)
        } else {
            return "";
        }
    }
}
