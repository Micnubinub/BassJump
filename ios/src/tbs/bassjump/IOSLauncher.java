package tbs.bassjump;


import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.pods.google.mobileads.GADInterstitial;
import org.robovm.pods.google.mobileads.GADInterstitialDelegateAdapter;
import org.robovm.pods.google.mobileads.GADRequest;

import java.util.Arrays;

public class IOSLauncher extends IOSApplication.Delegate {
    //Todo get adID
    private static final String adID = "ca-app-pub-6350309116730071/6868701948";
    private static GADInterstitial interstitial;
    private static UIViewController viewController;
    private static GADRequest request;

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    private static GADRequest createRequest() {
        GADRequest request = new GADRequest();
        // To test on your devices, add their UDIDs here:
        request.setTestDevices(Arrays.asList(GADRequest.getSimulatorID()));
        return request;
    }

    public static void showAd() {
        Game.log("showAd");
        if (interstitial.isReady())
            interstitial.present(viewController);
        else
            loadAd();

    }

    public static void loadAd() {
        Game.log("loadAd");
        if (request == null)
            request = createRequest();
        interstitial.loadRequest(request);
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        interstitial = createAndLoadInterstitial();
        final IOSApplication application = new IOSApplication(new Game(), config);
        viewController = application.getUIViewController();
        return application;
    }

    private GADInterstitial createAndLoadInterstitial() {
        GADInterstitial interstitial = new GADInterstitial(adID);
        interstitial.setDelegate(new GADInterstitialDelegateAdapter() {
            @Override
            public void didDismissScreen(GADInterstitial ad) {
                IOSLauncher.interstitial = createAndLoadInterstitial();
            }
        });
        loadAd();
        return interstitial;
    }
}