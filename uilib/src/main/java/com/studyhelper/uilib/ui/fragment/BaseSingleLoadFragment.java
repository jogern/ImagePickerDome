package com.studyhelper.uilib.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * Create on 2019/7/8.
 * @author jogern
 */
public abstract class BaseSingleLoadFragment extends BaseFragFragment {

      private View    mView;
      private boolean isLoad;

      @Override
      public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
            if (mView == null) {
                  mView = onInflaterView(inflater, container);
                  isLoad = true;
            }
            if (mView != null) {
                  ViewGroup parent = (ViewGroup) mView.getParent();
                  if (parent != null) {
                        parent.removeView(mView);
                  }
            }
            onCreateView(savedInstanceState);
            return mView;
      }

      @Override
      public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (isLoad) {
                  isLoad = false;
                  initialView(view);
            }
            onViewCreated(savedInstanceState);
      }

      protected void onCreateView(Bundle savedInstanceState) {}

      protected void onViewCreated(Bundle savedInstanceState) {}

      /**
       * 初始化
       * @param view
       */
      protected  void initialView(@NonNull View view){}

      /**
       * 加载View
       * @param inflater
       * @param container
       * @return View
       */
      protected abstract View onInflaterView(@NonNull LayoutInflater inflater, ViewGroup container);


}
