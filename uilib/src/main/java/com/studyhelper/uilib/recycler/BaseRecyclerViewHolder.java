package com.studyhelper.uilib.recycler;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Create on 17-9-14.
 * @author jogern
 */

public abstract class BaseRecyclerViewHolder<A extends BaseRecyclerItemAdapter> {

      private RecyclerHolder mHolder;
      private A              mItemAdapter;

      void setHolder(A itemAdp, RecyclerHolder holder) {
            mItemAdapter = itemAdp;
            mHolder = holder;
      }

      /**
       * Item的View的类型得到布局
       * @return 布局LayoutId
       */
      public abstract int getLayout();

      public A getItemAdapter() {
            return mItemAdapter;
      }

      /**
       * 获取item的ItemView
       * @return View
       */
      public View getItemView() {
            return mHolder.itemView;
      }

      /**
       * 获取itemView的Context
       * @return Context
       */
      public Context getContext() {
            return mHolder.getContext();
      }

      /**
       * 从itemView中查找控件
       * @param id 控件的Id
       * @return 控件（View）
       */
      protected <T extends View> T findViewById(int id) {
            return (T) mHolder.findViewById(id);
      }

      /**
       * viewHolder创建时调用，可以用来初始化控件或设置控件的监听事件
       */
      protected void initHolderView() {}

      /**
       * 绑定数据
       * @param position 索引
       */
      protected void onBinder(int position) {}

      /**
       * 绑定数据，方法中调用了{@link #onBinder(int)}
       * @param position
       * @param payloads
       */
      protected void onBinder(int position, List<Object> payloads) {
            onBinder(position);
      }

      /**
       * 当Item被回收的时候调用
       */
      protected void onViewRecycled() {}

      @Override
      protected void finalize() throws Throwable {
            mHolder = null;
            super.finalize();
      }

      /***********************下面的是ViewHolder中公共的方法*****************************/

      public final int getLayoutPosition() {
            return mHolder.getLayoutPosition();
      }

      public final int getAdapterPosition() {
            return mHolder.getAdapterPosition();
      }

      public final int getOldPosition() {
            return mHolder.getOldPosition();
      }

      public final void setIsRecyclable(boolean recyclable) {
            mHolder.setIsRecyclable(recyclable);
      }

      public final boolean isRecyclable() {
            return mHolder.isRecyclable();
      }
}