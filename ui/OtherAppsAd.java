package tbs.jumpsnew.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.R;
import tbs.jumpsnew.utility.Utility;

/**
 * Created by root on 5/01/15.
 */
public class OtherAppsAd {
    private static final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    private static final DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private static View view;
    private static ViewGroup layout;
    private static float animatedValue;
    private static final ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            animatedValue = ((Float) animation.getAnimatedValue());
            update();
        }
    };
    private static AnimationType animationType;
    private static final ValueAnimator.AnimatorListener listener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            switch (animationType) {
                case IN:
                    animationType = AnimationType.IDLING;
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hide();
                        }
                    }, 4800);
                    break;
                case OUT:
                    try {
                        layout.removeView(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case IDLING:
                    hide();
                    break;
            }
        }
    };
    private static int viewHeight;

    public OtherAppsAd(final Context context, RelativeLayout layout) {
        OtherAppsAd.layout = layout;
        view = View.inflate(context, R.layout.other_apps, null);
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=The+Big+Shots")));
                Utility.getPrefs(Game.context).put(Utility.CHECKOUT_OUR_OTHER_APPS, Utility.CHECKOUT_OUR_OTHER_APPS);
            }
        });
        viewHeight = dpToPixels(72);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, viewHeight));
        animator.setDuration(600);
        animator.setInterpolator(interpolator);
        animator.addUpdateListener(updateListener);
        animator.addListener(listener);
    }

    public static int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                MainActivity.context.getResources().getDisplayMetrics());
    }

    public static void show(int delay) {
        try {
            layout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        layout.addView(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    view.setY(-viewHeight);
                    animationType = AnimationType.IN;
                    startAnimator();
                }
            }, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startAnimator() {
        if (animator.isRunning()) {
            animator.cancel();
        }
        animator.start();
    }

    public static void hide() {
        view.setY(-viewHeight);
        animationType = AnimationType.OUT;
        startAnimator();
    }

    private static void update() {
        switch (animationType) {
            case IN:
                view.setY((viewHeight * animatedValue) - viewHeight);
                break;
            case OUT:
                view.setY(-(viewHeight * animatedValue));
                break;
            case IDLING:
                view.setY(0);
                break;
        }
        view.invalidate();
        layout.requestLayout();
    }

    private enum AnimationType {
        IN, OUT, IDLING
    }
}
