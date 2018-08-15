package com.appodealx.tapjoy;

import com.appodealx.sdk.AdError;
import com.appodealx.sdk.FullScreenAdListener;
import com.tapjoy.TJPlacement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TapjoyFullScreenAdListenerTest {

    private TapjoyFullScreenAd tapjoyFullScreenAd;
    private FullScreenAdListener fullScreenAdListener;

    @Before
    public void setUp() throws Exception {
        tapjoyFullScreenAd = mock(TapjoyFullScreenAd.class);
        fullScreenAdListener = mock(FullScreenAdListener.class);
    }

    @Test
    public void onRequestSuccess_nofill() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        TJPlacement placement = mock(TJPlacement.class);
        when(placement.isContentAvailable()).thenReturn(false);
        tapjoyFullScreenAdListener.onRequestSuccess(placement);

        verify(fullScreenAdListener).onFullScreenAdFailedToLoad(AdError.NoFill);
    }

    @Test
    public void onRequestSuccess_do_nothing() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        TJPlacement placement = mock(TJPlacement.class);
        when(placement.isContentAvailable()).thenReturn(true);
        tapjoyFullScreenAdListener.onRequestSuccess(placement);

        verify(fullScreenAdListener, times(0)).onFullScreenAdFailedToLoad(AdError.NoFill);
    }

    @Test
    public void onRequestFailure() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        tapjoyFullScreenAdListener.onRequestFailure(null, null);

        verify(fullScreenAdListener).onFullScreenAdFailedToLoad(AdError.NoFill);
    }

    @Test
    public void onContentReady() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        tapjoyFullScreenAdListener.onContentReady(null);

        verify(fullScreenAdListener).onFullScreenAdLoaded(tapjoyFullScreenAd);
    }

    @Test
    public void onContentDismiss() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        tapjoyFullScreenAdListener.onContentDismiss(null);

        verify(fullScreenAdListener).onFullScreenAdClosed(false);
    }

    @Test
    public void onContentDismiss_after_onVideoComplete() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        tapjoyFullScreenAdListener.onVideoComplete(null);
        tapjoyFullScreenAdListener.onContentDismiss(null);

        verify(fullScreenAdListener).onFullScreenAdCompleted();
        verify(fullScreenAdListener).onFullScreenAdClosed(true);
    }

    @Test
    public void onContentShow() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        tapjoyFullScreenAdListener.onContentShow(null);

        verify(fullScreenAdListener).onFullScreenAdShown();
    }

    @Test
    public void onVideoComplete() throws Exception {
        TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(tapjoyFullScreenAd, fullScreenAdListener);
        tapjoyFullScreenAdListener.onVideoComplete(null);

        verify(fullScreenAdListener).onFullScreenAdCompleted();
    }
}