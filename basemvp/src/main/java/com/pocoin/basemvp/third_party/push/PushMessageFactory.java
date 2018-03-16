package com.pocoin.basemvp.third_party.push;

import android.content.Intent;

/**
 * Created by Robert yao on 2016/11/8.
 */
public interface PushMessageFactory {
    PushMessage intentToMessage(Intent intent);
}
