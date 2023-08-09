package cc.jooylife.meerkat.collector.task;

import cc.jooylife.meerkat.core.service.SymbolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SymbolTask {

    private final SymbolService symbolService;

    @Autowired
    public SymbolTask(SymbolService symbolService) {
        this.symbolService = symbolService;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void syncSymbol() {
        try {
            symbolService.syncSymbol();
        } catch (Exception e) {
            log.error("Sync symbols error", e);
        }
    }
}
