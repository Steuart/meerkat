package cc.jooylife.meerkat.core.common.enums;

public enum SymbolStatusEnum {

    /**
     * 交易对状态
     */
    TRADING("TRADING", "交易中"),
    BREAK("BREAK", "暂停"),
    ;

    public final String code;

    public final String desc;

    SymbolStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
