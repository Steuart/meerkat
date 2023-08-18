package cc.jooylife.meerkat.core.common.enums;

/**
 * @Author: wuhaiming
 * @Date: 2023/8/18 16:11
 */
public enum SymbolStatusEnum {

    ENABLE("enable", "启动"),
    DISABLE("disable", "禁用"),
    ;

    public final String code;

    public final String desc;

    SymbolStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
