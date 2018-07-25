package com.BetterTogether.app;

public interface TokenListener {

    void onTokenReceived(String token);

    void tokenRejected();
}
