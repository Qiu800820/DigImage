package com.pocoin.basemvp.third_party.app_analysis;

/**
 * Created by Robert yao on 2016/11/7.
 */

public interface AppUserLoginAnalysis {
    void loginSuccess(String userId);
    void thirdPartyLoginSuccess(String thirdPartyLibName, String userId);
    void logout();
}
