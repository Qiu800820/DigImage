package com.pocoin.basemvp.third_party.push;

import java.util.Set;

/**
 * Created by Robert yao on 2016/11/8.
 */

public interface PushClient{

    void init();
    void destroy();
    void resumePush();
    void setAlias(String alias);
    void setTag(Set<String> tags);
}
