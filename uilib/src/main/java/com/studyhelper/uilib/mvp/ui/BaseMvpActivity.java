package com.studyhelper.uilib.mvp.ui;

import android.os.Bundle;

import com.studyhelper.uilib.mvp.PresenterHolder;
import com.studyhelper.uilib.mvp.BoardView;
import com.studyhelper.uilib.mvp.Presenter;
import com.studyhelper.uilib.ui.BaseFragActivity;

/**
 * Create on 2019/7/4.
 * @author jogern
 */
public abstract class BaseMvpActivity<V extends BoardView, P extends Presenter> extends BaseFragActivity {

      private PresenterHolder<V, P> mPresenter;

//      protected P getPresenter() {
//            if (mPresenter != null) {
//                  return mPresenter.getPresenter();
//            }
//            return null;
//      }
//
//      protected V getViewBoard() {
//            if (mPresenter != null) {
//                  return mPresenter.getViewBoard();
//            }
//            return null;
//      }

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPresenter = createPresenter();
            mPresenter.onCreated();
      }


      @Override
      protected void onDestroy() {
            super.onDestroy();
            mPresenter.onDestroyed();
            mPresenter = null;
      }

      protected abstract PresenterHolder<V, P> createPresenter();

}
