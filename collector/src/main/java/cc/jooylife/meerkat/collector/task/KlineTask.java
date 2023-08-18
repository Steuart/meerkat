package cc.jooylife.meerkat.collector.task;

import cc.jooylife.meerkat.core.common.enums.KlineTypeEnum;
import cc.jooylife.meerkat.core.service.KlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class KlineTask {

    private final KlineService klineService;

    private volatile boolean klineSyncRunning = false;

    @Autowired
    public KlineTask(KlineService klineService) {
        this.klineService = klineService;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void syncKline() {
        if (klineSyncRunning) {
            log.error("Kline sync is running, skip this time");
        }
        try {
            klineSyncRunning = true;
            klineService.syncKlineData(new Date());
        } catch (Exception e) {
            log.error("syncKline error", e);
        } finally {
            klineSyncRunning = false;
        }
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void checkWebsocket() {
        try {
            klineService.reconnectWebsocket(KlineTypeEnum.FIVE_MINUTE);
        } catch (Exception e) {
            log.error("Check web socket error", e);
        }
    }
}
