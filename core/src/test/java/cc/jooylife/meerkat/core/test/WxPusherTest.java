package cc.jooylife.meerkat.core.test;

import cc.jooylife.environment.EnvUtil;
import cc.jooylife.meerkat.core.util.JsonUtil;
import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author: wuhaiming
 * @Date: 2023/8/18 17:56
 */
@Slf4j
public class WxPusherTest {

    @Test
    public void pushTest() {
        String appToken = EnvUtil.getValue("meerkat.wxpusher.app-token");
        Message message = new Message();
        message.setAppToken(appToken);
        message.setContentType(Message.CONTENT_TYPE_TEXT);
        message.setContent("不加限制的自由是很可怕的，因为很容易让任何人滑向深渊。");
        message.setUid("UID_z9EgPrIPi4YjD55ECmZC4XDkz8CR");
        message.setUrl("http://wxpuser.zjiecode.com");//可选参数
        Result<List<MessageResult>> result = WxPusher.send(message);
        log.info("result:{}", JsonUtil.toJson(result));
    }
}
