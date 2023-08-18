package cc.jooylife.meerkat.core.common.enums;

import com.binance.api.client.domain.market.CandlestickInterval;

public enum KlineTypeEnum {

        /**
        * K线间隔
        */
        ONE_MINUTE("1m", "1分钟"),
        THREE_MINUTE("3m", "3分钟"),
        FIVE_MINUTE("5m", "5分钟"),
        FIFTEEN_MINUTE("15m", "15分钟"),
        THIRTY_MINUTE("30m", "30分钟"),
        ONE_HOUR("1h", "1小时"),
        TWO_HOUR("2h", "2小时"),
        FOUR_HOUR("4h", "4小时"),
        SIX_HOUR("6h", "6小时"),
        EIGHT_HOUR("8h", "8小时"),
        TWELVE_HOUR("12h", "12小时"),
        ONE_DAY("1d", "1天"),
        THREE_DAY("3d", "3天"),
        ONE_WEEK("1w", "1周"),
        ONE_MONTH("1M", "1月"),
        ;

        public final String code;

        public final String desc;

        KlineTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static CandlestickInterval getByCode(KlineTypeEnum type) {
            for (CandlestickInterval CandlestickInterval : CandlestickInterval.values()) {
                if (CandlestickInterval.getIntervalId().equals(type.code)) {
                    return CandlestickInterval;
                }
            }
            return CandlestickInterval.FIVE_MINUTES;
        }
}
