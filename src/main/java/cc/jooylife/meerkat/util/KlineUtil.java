package cc.jooylife.meerkat.util;

public class KlineUtil {
    /**
     * 请求参数存取
     */
    private static final ThreadLocal<String> SYMBOL = new ThreadLocal<>();

    /**
     * 设置请求参数
     */
    public static void setSymbol(String symbol) {
        SYMBOL.set(symbol);
    }

    public static String getSymbol() {
        return SYMBOL.get();
    }
}
