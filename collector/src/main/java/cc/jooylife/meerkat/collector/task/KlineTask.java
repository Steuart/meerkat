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

    @Autowired
    public KlineTask(KlineService klineService) {
        this.klineService = klineService;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void syncKline() {
        try {
            klineService.syncKlines(new Date());
        } catch (Exception e) {
            log.error("syncKline error", e);
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
