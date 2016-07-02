// Generated code from Butter Knife. Do not modify!
package com.example.gursimransingh.greenbus_evs_iiitd.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ShareExperience_Fragment$$ViewInjector<T extends com.example.gursimransingh.greenbus_evs_iiitd.fragments.ShareExperience_Fragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624116, "field 'feedbackBusNumber'");
    target.feedbackBusNumber = finder.castView(view, 2131624116, "field 'feedbackBusNumber'");
    view = finder.findRequiredView(source, 2131624117, "field 'edittext_feedback'");
    target.edittext_feedback = finder.castView(view, 2131624117, "field 'edittext_feedback'");
  }

  @Override public void reset(T target) {
    target.feedbackBusNumber = null;
    target.edittext_feedback = null;
  }
}
