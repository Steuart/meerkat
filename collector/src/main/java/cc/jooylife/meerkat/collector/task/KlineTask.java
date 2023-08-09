package cc.jooylife.meerkat.collector.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class KlineTask {


    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void syncKline() {
        log.info("Sync kline");
    }
}
