package com.studyhelper.uilib.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create on 2019/7/8.
 * @author jogern
 */
public abstract class BaseSingleLoadFragment extends BaseFragFragment {

      private View    mView;
      private boolean isLoad;

      @Override
      public final View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
            if (mView == null) {
                  mView = onInflaterView(inflater, container);
                  isLoad = true;
            }
            onCreateView(savedInstanceState);
            if (mView != null) {
                  ViewGroup parent = (ViewGroup) mView.getParent();
                  if (parent != null) {
                        parent.removeView(mView);
                  }
                  return mView;
            }
            return super.onCreateView(inflater, container, savedInstanceState);
      }

      @Override
      public final void onViewCreated(View view,Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            onViewCreated(savedInstanceState);
            if (isLoad) {
                  isLoad = false;
                  initialView(view);
            }
      }

      protected void onCreateView(Bundle savedInstanceState) {}

      protected void onViewCreated(Bundle savedInstanceState) {}

      /**
       * 初始化
       * @param view
       */
      protected  void initialView(View view){}

      /**
       * 加载View
       * @param inflater
       * @param container
       * @return View
       */
      protected abstract View onInflaterView(LayoutInflater inflater, ViewGroup container);


}
