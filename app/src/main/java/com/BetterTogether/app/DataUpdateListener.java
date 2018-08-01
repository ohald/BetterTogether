package com.BetterTogether.app;

public interface DataUpdateListener {

    void tokenReceived(String token);

    void tokenRejected();

    void updateGrid();

    void updateStatus();
}
