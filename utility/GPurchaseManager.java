package tbs.jumpsnew.utility;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.android.vending.billing.IInAppBillingService;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.MainActivity;

public class GPurchaseManager {
    public final ServiceConnection mServiceConn;
    public IInAppBillingService mService;

    public GPurchaseManager() {
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName arg0, IBinder arg1) {
                mService = IInAppBillingService.Stub.asInterface(arg1);

            }
        };
        Intent serviceIntent = new Intent(
                "com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        Game.context.bindService(serviceIntent, mServiceConn,
                Context.BIND_AUTO_CREATE);
    }

    public void makePurchase(String itemID) {
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3,
                    MainActivity.context.getPackageName(), itemID, "inapp",
                    "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            PendingIntent pendingIntent = buyIntentBundle
                    .getParcelable("BUY_INTENT");
            ((Activity) Game.context).startIntentSenderForResult(
                    pendingIntent.getIntentSender(), 1001, new Intent(),
                    0, 0, 0);
        } catch (Exception e) {
            // FAILED TO MAKE PURCHASE:
            Utility.showToast("Failed to Make Purchase!", Game.context);
            e.printStackTrace();
        }
    }
}
