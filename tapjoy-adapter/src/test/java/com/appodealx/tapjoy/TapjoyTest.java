package com.appodealx.tapjoy;

import android.app.Activity;

import com.appodeal.ads.test_utils.TestUtils;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TapjoyTest {

    private Activity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(Activity.class).create().start().get();
    }

    @Test
    public void getInterstitialRequestInfo() throws Exception {
        Tapjoy tapjoy = new Tapjoy();
        JSONObject adunit = new JSONObject(TestUtils.getStringFromFile(activity, "video/tapjoy.json"));
        JSONArray jsonArray = tapjoy.getInterstitialRequestInfo(adunit);

        JSONObject requestInfo = jsonArray.getJSONObject(0);
        JSONObject ext = requestInfo.getJSONObject("ext");
        Assert.assertEquals(ext.getString("sdk_key"), adunit.getString("sdk_key"));
        Assert.assertEquals(ext.getString("placement_name"), adunit.getString("placement"));

        JSONObject appodeal = requestInfo.getJSONObject("appodeal");
        Assert.assertEquals(appodeal.getString("id"), adunit.getString("id"));
        Assert.assertEquals(appodeal.getString("ecpm"), adunit.getString("ecpm"));

        Assert.assertEquals(requestInfo.getString("displaymanager"), "tapjoy");
        Assert.assertEquals(requestInfo.getString("displaymanager_ver"), "12.0.0");
    }
}