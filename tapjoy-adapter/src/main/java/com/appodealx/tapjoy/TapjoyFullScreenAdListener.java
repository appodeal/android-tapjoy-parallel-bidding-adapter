package com.appodealx.tapjoy;

import com.appodealx.sdk.AdError;
import com.appodealx.sdk.FullScreenAdListener;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.TJPlacementVideoListener;

public class TapjoyFullScreenAdListener implements TJPlacementListener, TJPlacementVideoListener {

    private TapjoyFullScreenAd fullScreenAd;
    private FullScreenAdListener listener;
    private boolean finished;

    TapjoyFullScreenAdListener(TapjoyFullScreenAd tapjoyFullScreenAd, FullScreenAdListener listener) {
        this.fullScreenAd = tapjoyFullScreenAd;
        this.listener = listener;
    }

    @Override
    public void onRequestSuccess(TJPlacement tjPlacement) {
        if (!tjPlacement.isContentAvailable()) {
            listener.onFullScreenAdFailedToLoad(AdError.NoFill);
        }
    }

    @Override
    public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {
        listener.onFullScreenAdFailedToLoad(AdError.NoFill);
    }

    @Override
    public void onContentReady(TJPlacement tjPlacement) {
        listener.onFullScreenAdLoaded(fullScreenAd);
    }

    @Override
    public void onContentShow(TJPlacement tjPlacement) {
        listener.onFullScreenAdShown();
    }

    @Override
    public void onContentDismiss(TJPlacement tjPlacement) {
        listener.onFullScreenAdClosed(finished);
    }

    @Override
    public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {
    }

    @Override
    public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {
    }

    @Override
    public void onVideoStart(TJPlacement tjPlacement) {

    }

    @Override
    public void onVideoError(TJPlacement tjPlacement, String s) {
    }

    @Override
    public void onVideoComplete(TJPlacement tjPlacement) {
        finished = true;
        listener.onFullScreenAdCompleted();
    }
}
