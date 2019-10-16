package com.studyhelper.uilib.mvp;



import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Create on 2019/7/4.
 * @author jogern
 */
public class PresenterHolder<V extends BoardView, P extends Presenter> {

      private WeakReference<P> mPresenter;
      private WeakReference<V> mViewBoard;

      @Nullable
      public V getViewBoard() {
            if (mViewBoard == null) {
                  return null;
            }
            return mViewBoard.get();
      }

      @Nullable
      public P getPresenter() {
            if (mPresenter == null) {
                  return null;
            }
            return mPresenter.get();
      }

      public PresenterHolder<V, P> attachViewBoard(V viewBoard) {
            mViewBoard = new WeakReference<>(viewBoard);
            return this;
      }

      public PresenterHolder<V, P> attachPresenter(P presenter) {
            mPresenter = new WeakReference<>(presenter);
            return this;
      }

      /**
       * 如果是Activity与Activity的onCreate关联<p/>
       * 如果是Fragment与Fragment的onAttach关联
       */
      public void onCreated() { }

      /**
       * 如果是Activity与Activity的onDestroy关联<p/>
       * 如果是Fragment与Fragment的onDetach关联
       */
      public void onDestroyed() { }

}
