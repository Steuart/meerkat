package cc.jooylife.meerkat.collector.task;

import cc.jooylife.meerkat.core.service.KlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class KlineTask {

    private final KlineService klineService;

    @Autowired
    public KlineTask(KlineService klineService) {
        this.klineService = klineService;
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void syncKline() {
        try {
            klineService.syncKlines();
        } catch (Exception e) {
            log.error("syncKline error", e);
        }
    }
}
