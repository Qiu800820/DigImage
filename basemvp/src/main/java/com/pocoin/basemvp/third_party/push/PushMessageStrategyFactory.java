package com.pocoin.basemvp.third_party.push;

import com.jiongbull.jlog.JLog;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Robert yao on 2016/11/9.
 */
public class PushMessageStrategyFactory {
    public static final int DEFAULT_TYPE = 0x0000;
    private static PushMessageStrategyFactory INSTANCE = null;
    private HashMap<Integer, Constructor<? extends PushMessageStrategy>> pushMessageStrategyHashMap;
    public static PushMessageStrategyFactory getInstance() {
        if (null == INSTANCE){
            INSTANCE = new PushMessageStrategyFactory();
        }
        return INSTANCE;
    }

    private PushMessageStrategyFactory() {
        pushMessageStrategyHashMap = new LinkedHashMap<>();
    }

    public void registerReceiverStrategy(int type, Constructor<? extends PushMessageStrategy> pushMessageStrategyClass){
        pushMessageStrategyHashMap.put(type, pushMessageStrategyClass);
    }

    public PushMessageStrategy createStrategy(PushMessage pushMessage) throws Exception {

        Constructor<? extends PushMessageStrategy> pushMessageStrategyClass= pushMessageStrategyHashMap.get(pushMessage.getMsgType());

        if(pushMessageStrategyClass == null){
            JLog.w("Unknown pushMessage Type");
            pushMessageStrategyClass = pushMessageStrategyHashMap.get(DEFAULT_TYPE);
        }


        return pushMessageStrategyClass.newInstance(pushMessage);
    }
}
