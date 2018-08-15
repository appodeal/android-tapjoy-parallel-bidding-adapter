package com.appodealx.tapjoy;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.appodealx.sdk.AdError;
import com.appodealx.sdk.FullScreenAdListener;
import com.appodealx.sdk.InternalAdapterInterface;
import com.appodealx.sdk.utils.RequestInfoKeys;
import com.tapjoy.TJConnectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Iterator;

public class Tapjoy extends InternalAdapterInterface {

    private String token;

    @Override
    public void initialize(@NonNull Activity activity, @NonNull JSONObject adapterSettings) {
        try {
            String tapjoySDKKey = adapterSettings.getString("sdk_key");
            if (!com.tapjoy.Tapjoy.isConnected()) {
                com.tapjoy.Tapjoy.connect(activity, tapjoySDKKey, new Hashtable<String, Object>(), new TJConnectListener() {
                    @Override
                    public void onConnectSuccess() {
                        token = com.tapjoy.Tapjoy.getUserToken();
                    }

                    @Override
                    public void onConnectFailure() {
                    }
                });
            } else {
                token = com.tapjoy.Tapjoy.getUserToken();
            }
        } catch (JSONException e) {
            Log.e("Appodealx-Tapjoy", e.getMessage());
        }
    }

    @Override
    public void updateConsent(Activity activity, boolean hasConsent, boolean isGDPRScope) {
        com.tapjoy.Tapjoy.setUserConsent(hasConsent ? "1" : "0");
        com.tapjoy.Tapjoy.subjectToGDPR(isGDPRScope);
    }

    @Override
    public void loadInterstitial(@NonNull Activity activity, JSONObject adInfo, FullScreenAdListener listener) {
        if (com.tapjoy.Tapjoy.isConnected()) {
            TapjoyFullScreenAd tapjoyFullScreenAd = new TapjoyFullScreenAd(adInfo, listener);
            tapjoyFullScreenAd.load(activity);
        } else {
            listener.onFullScreenAdFailedToLoad(AdError.SDKNotInitialized);
        }
    }

    @NonNull
    @Override
    public JSONArray getInterstitialRequestInfo(@NonNull JSONObject adapterSettings) {
        JSONArray adTypeInfo = new JSONArray();
        JSONObject info = getRequestInfoFromSettings(adapterSettings);
        if (info != null) {
            adTypeInfo.put(info);
        }
        return adTypeInfo;
    }

    @Override
    public void loadRewardedVideo(@NonNull Activity activity, JSONObject adInfo, FullScreenAdListener listener) {
        if (com.tapjoy.Tapjoy.isConnected()) {
            TapjoyFullScreenAd tapjoyFullScreenAd = new TapjoyFullScreenAd(adInfo, listener);
            tapjoyFullScreenAd.load(activity);
        } else {
            listener.onFullScreenAdFailedToLoad(AdError.SDKNotInitialized);
        }
    }

    @NonNull
    @Override
    public JSONArray getRewardedVideoRequestInfo(@NonNull JSONObject adapterSettings) {
        JSONArray adTypeInfo = new JSONArray();
        JSONObject info = getRequestInfoFromSettings(adapterSettings);
        if (info != null) {
            adTypeInfo.put(info);
        }
        return adTypeInfo;
    }

    @Nullable
    private JSONObject getRequestInfoFromSettings(JSONObject settings) {
        try {
            JSONObject info = new JSONObject();
            info.put(RequestInfoKeys.DISPLAY_MANAGER, "tapjoy");
            info.put(RequestInfoKeys.DISPLAY_MANAGER_VERSION, com.tapjoy.Tapjoy.getVersion());

            JSONObject ext = new JSONObject();
            ext.put("sdk_key", settings.getString("sdk_key"));
            ext.put("placement_name", settings.getString("placement"));
            if (!TextUtils.isEmpty(token)) {
                ext.put("token", token);
            }

            JSONObject adUnitExt = settings.optJSONObject(RequestInfoKeys.EXTRA_PARALLEL_BIDDING_INFO);
            if (adUnitExt != null) {
                Iterator<?> keys = adUnitExt.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    ext.put(key, adUnitExt.get(key));
                }
            }
            info.put(RequestInfoKeys.EXT, ext);

            JSONObject appodealInfo = new JSONObject();
            appodealInfo.put(RequestInfoKeys.APPODEAL_ID, settings.getString("id"));
            appodealInfo.put(RequestInfoKeys.APPODEAL_ECPM, settings.getDouble("ecpm"));
            info.put(RequestInfoKeys.APPODEAL_INFO, appodealInfo);

            return info;
        } catch (JSONException e) {
            Log.e("Appodealx-Tapjoy", e.getMessage());
        }
        return null;
    }
}