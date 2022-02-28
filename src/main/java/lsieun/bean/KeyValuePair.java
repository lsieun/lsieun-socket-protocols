package lsieun.bean;

public class KeyValuePair {
    private static final String TO_STRING_FORMAT = "%s: %s";
    public final String key;
    public final String value;

    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, key, value);
    }
}
