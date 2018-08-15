package com.appodealx.tapjoy;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appodealx.sdk.AdError;
import com.appodealx.sdk.FullScreenAd;
import com.appodealx.sdk.FullScreenAdListener;
import com.tapjoy.TJPlacement;
import com.tapjoy.TapjoyAuctionFlags;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

class TapjoyFullScreenAd extends FullScreenAd {

    private JSONObject adInfo;
    private FullScreenAdListener listener;
    private TJPlacement placement;

    TapjoyFullScreenAd(JSONObject adInfo, FullScreenAdListener listener) {
        this.adInfo = adInfo;
        this.listener = listener;
    }

    public void load(Activity activity) {
        try {
            TapjoyFullScreenAdListener tapjoyFullScreenAdListener = new TapjoyFullScreenAdListener(this, listener);
            String placementName = adInfo.getString("placement_name");
            com.tapjoy.Tapjoy.setActivity(activity);
            placement = com.tapjoy.Tapjoy.getPlacement(placementName, tapjoyFullScreenAdListener);
            placement.setMediationName("appodeal");
            placement.setAdapterVersion("0.0.1");
            placement.setVideoListener(tapjoyFullScreenAdListener);

            HashMap<String, String> auctionData = new HashMap<>();
            JSONObject tapjoyMetadata = adInfo.getJSONObject("tapjoy_metadata");
            String id = tapjoyMetadata.getString(TapjoyAuctionFlags.AUCTION_ID);
            String extData = tapjoyMetadata.getString(TapjoyAuctionFlags.AUCTION_DATA);

            auctionData.put(TapjoyAuctionFlags.AUCTION_ID, id);
            auctionData.put(TapjoyAuctionFlags.AUCTION_DATA, extData);
            placement.setAuctionData(auctionData);
            placement.requestContent();
        } catch (JSONException e) {
            Log.e("Appodealx-Tapjoy", e.getMessage());
            listener.onFullScreenAdFailedToLoad(AdError.InternalError);
        }

    }

    @Override
    public void show(@NonNull Activity activity) {
        if (placement != null && placement.isContentReady()) {
            com.tapjoy.Tapjoy.setActivity(activity);
            placement.showContent();
        } else {
            listener.onFullScreenAdFailedToShow(AdError.InternalError);
        }
    }
}
