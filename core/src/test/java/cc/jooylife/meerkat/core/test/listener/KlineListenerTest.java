package cc.jooylife.meerkat.core.test.listener;

import cc.jooylife.meerkat.core.listener.KlineListener;
import cc.jooylife.meerkat.core.test.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest(classes = Application.class)
@Slf4j
public class KlineListenerTest {

    @Autowired
    private KlineListener klineListener;

    @Test
    public void test() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        klineListener.startListener();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
