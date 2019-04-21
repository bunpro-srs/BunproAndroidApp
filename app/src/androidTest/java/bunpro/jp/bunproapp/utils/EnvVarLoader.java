package bunpro.jp.bunproapp.utils;

public class EnvVarLoader {

    static public String getEnvironmentVariable(String key) {
        return System.getProperty(key);
    }
}
