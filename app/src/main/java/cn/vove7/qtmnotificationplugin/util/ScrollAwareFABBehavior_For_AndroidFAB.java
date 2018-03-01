package cn.vove7.qtmnotificationplugin.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * Created by Vove on 2017/11/2.
 *android.support.design.widget.FloatingActionButton 滑动自动隐藏
 */

public class ScrollAwareFABBehavior_For_AndroidFAB extends FloatingActionButton.Behavior {
   private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
   private boolean mIsAnimatingOut = false;

   public ScrollAwareFABBehavior_For_AndroidFAB(Context context, AttributeSet attrs) {
      super();
   }

   @Override
   public boolean onStartNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final FloatingActionButton child,
                                      @NonNull final View directTargetChild, @NonNull final View target, final int nestedScrollAxes) {
      //  Ensure  we  react  to  vertical  scrolling
      return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
              || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
   }

   @Override
   public void onNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final FloatingActionButton child,
                              @NonNull final View target, final int dxConsumed, final int dyConsumed,
                              final int dxUnconsumed, final int dyUnconsumed) {
      super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
      if (dyConsumed > 0 && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
         //  User  scrolled  down  and  the  FAB  is  currently  visible  ->  hide  the  FAB
         animateOut(child);
      } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
         //  User  scrolled  up  and  the  FAB  is  currently  not  visible  ->  show  the  FAB
         animateIn(child);
      }
   }

   //  Same  animation  that  FloatingActionButton.Behavior  uses  to  hide  the  FAB  when  the  AppBarLayout  exits
   private void animateOut(final FloatingActionButton button) {
      ViewCompat.animate(button).translationY(button.getHeight() + getMarginBottom(button)).setInterpolator(INTERPOLATOR).withLayer()
              .setListener(new ViewPropertyAnimatorListener() {
                 public void onAnimationStart(View view) {
                    ScrollAwareFABBehavior_For_AndroidFAB.this.mIsAnimatingOut = true;
                 }

                 public void onAnimationCancel(View view) {
                    ScrollAwareFABBehavior_For_AndroidFAB.this.mIsAnimatingOut = false;
                 }

                 public void onAnimationEnd(View view) {
                    ScrollAwareFABBehavior_For_AndroidFAB.this.mIsAnimatingOut = false;
                    view.setVisibility(View.INVISIBLE);
                 }
              }).start();
   }

   //  Same  animation  that  FloatingActionButton.Behavior  uses  to  show  the  FAB  when  the  AppBarLayout  enters
   private void animateIn(FloatingActionButton button) {
      button.setVisibility(View.VISIBLE);
      ViewCompat.animate(button).translationY(0)
              .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
              .start();
   }

   private int getMarginBottom(View v) {
      int marginBottom = 0;
      final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
      if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
         marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
      }
      return marginBottom;
   }
}