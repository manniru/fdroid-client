package org.fdroid.fdroid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;

public class NfcHelper {

    @TargetApi(14)
    private static NfcAdapter getAdapter(Context context) {
        if (Build.VERSION.SDK_INT < 14)
            return null;

        return NfcAdapter.getDefaultAdapter(context.getApplicationContext());
    }

    @TargetApi(14)
    public static boolean setPushMessage(Activity activity, Uri toShare) {
        NfcAdapter adapter = getAdapter(activity);
        if (adapter != null) {
            adapter.setNdefPushMessage(new NdefMessage(new NdefRecord[]{
                    NdefRecord.createUri(toShare),
            }), activity);
            return true;
        }
        return false;
    }

    @TargetApi(16)
    static void setAndroidBeam(Activity activity, String packageName) {
        if (Build.VERSION.SDK_INT < 16)
            return;
        PackageManager pm = activity.getPackageManager();
        NfcAdapter nfcAdapter = getAdapter(activity);
        if (nfcAdapter != null) {
            ApplicationInfo appInfo;
            try {
                appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                Uri[] uris = {
                        Uri.parse("file://" + appInfo.publicSourceDir),
                };
                nfcAdapter.setBeamPushUris(uris, activity);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(16)
    static void disableAndroidBeam(Activity activity) {
        if (Build.VERSION.SDK_INT < 16)
            return;
        NfcAdapter nfcAdapter = getAdapter(activity);
        if (nfcAdapter != null)
            nfcAdapter.setBeamPushUris(null, activity);
    }

}
