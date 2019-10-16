package com.studyhelper.uilib.mvp.ui.fragment;

import android.content.Context;
import com.studyhelper.uilib.mvp.PresenterHolder;
import com.studyhelper.uilib.mvp.BoardView;
import com.studyhelper.uilib.mvp.Presenter;
import com.studyhelper.uilib.ui.fragment.BaseFragFragment;

/**
 * Create on 2019/7/5.
 * @author jogern
 */
public abstract class BaseMvpFragment<V extends BoardView, P extends Presenter>
        extends BaseFragFragment {

      private PresenterHolder<V,P> mPresenterHolder;

//      @Nullable
//      protected P getPresenter() {
//            if (mPresenterHolder != null) {
//                  return mPresenterHolder.getPresenter();
//            }
//            return null;
//      }
//
//      @Nullable
//      protected V getViewBoard() {
//            if (mPresenterHolder != null) {
//                  return mPresenterHolder.getViewBoard();
//            }
//            return null;
//      }

      @Override
      public void onAttach(Context context) {
            super.onAttach(context);
            mPresenterHolder = createPresenter();
            if (mPresenterHolder == null) {
                  throw new RuntimeException("must create Presenter");
            }
            mPresenterHolder.onCreated();
      }


      @Override
      public void onDetach() {
            super.onDetach();
            mPresenterHolder.onDestroyed();
      }

      protected abstract PresenterHolder<V,P> createPresenter();

}
