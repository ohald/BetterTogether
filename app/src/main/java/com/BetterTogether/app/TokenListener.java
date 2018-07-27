package com.BetterTogether.app;

public interface TokenListener {

    void tokenReceived(String token);

    void tokenRejected();
}
