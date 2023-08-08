package cc.jooylife.meerkat.common.param;

import lombok.Data;

import java.util.Date;

@Data
public class KlineParam {

    private String symbol;

    private String interval;

    private Date startTime;

    private Date endTime;

    private Integer limit;
}
