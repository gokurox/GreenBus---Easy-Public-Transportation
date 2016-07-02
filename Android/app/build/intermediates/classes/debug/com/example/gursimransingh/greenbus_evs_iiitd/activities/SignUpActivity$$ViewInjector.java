// Generated code from Butter Knife. Do not modify!
package com.example.gursimransingh.greenbus_evs_iiitd.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class SignUpActivity$$ViewInjector<T extends com.example.gursimransingh.greenbus_evs_iiitd.activities.SignUpActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624086, "field '_nameText'");
    target._nameText = finder.castView(view, 2131624086, "field '_nameText'");
    view = finder.findRequiredView(source, 2131624080, "field '_emailText'");
    target._emailText = finder.castView(view, 2131624080, "field '_emailText'");
    view = finder.findRequiredView(source, 2131624081, "field '_passwordText'");
    target._passwordText = finder.castView(view, 2131624081, "field '_passwordText'");
    view = finder.findRequiredView(source, 2131624088, "field '_signupButton'");
    target._signupButton = finder.castView(view, 2131624088, "field '_signupButton'");
    view = finder.findRequiredView(source, 2131624089, "field '_loginLink'");
    target._loginLink = finder.castView(view, 2131624089, "field '_loginLink'");
    view = finder.findRequiredView(source, 2131624087, "field '_mobileText'");
    target._mobileText = finder.castView(view, 2131624087, "field '_mobileText'");
  }

  @Override public void reset(T target) {
    target._nameText = null;
    target._emailText = null;
    target._passwordText = null;
    target._signupButton = null;
    target._loginLink = null;
    target._mobileText = null;
  }
}
