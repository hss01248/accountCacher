package com.hss01248.accountcache;

/**
 * by hss
 * data:2020-04-27
 * desc:
 */
public interface AccountCallback {

    void  onSuccess(DebugAccount account);

    void onError(Throwable error);
}
