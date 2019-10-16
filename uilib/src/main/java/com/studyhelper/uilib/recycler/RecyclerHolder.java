package com.studyhelper.uilib.recycler;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView.ViewHolder做成一个中间类
 * Create on 17-9-14.
 * @author jogern
 */

public final class RecyclerHolder<A extends BaseRecyclerItemAdapter> extends RecyclerView.ViewHolder {

      private SparseArray<View>     mViews = new SparseArray<>();
      private BaseRecyclerViewHolder<A> mViewHolder;

      RecyclerHolder(View itemView, A adp, BaseRecyclerViewHolder<A> viewHolder) {
            super(itemView);
            mViewHolder = viewHolder;
            mViewHolder.setHolder(adp, this);
            viewHolder.initHolderView();
      }

      View findViewById(int id) {
            View viewById = mViews.get(id);
            if (viewById == null) {
                  viewById = itemView.findViewById(id);
                  mViews.put(id, viewById);
            }
            return viewById;
      }

      void onBinder(int position, List<Object> payloads) {
            mViewHolder.onBinder(position, payloads);
      }

      void onViewRecycled() {
            mViewHolder.onViewRecycled();
      }

      Context getContext() {
            return itemView.getContext();
      }

      @Override
      protected void finalize() throws Throwable {
            mViewHolder = null;
            mViews.clear();
            super.finalize();
      }
}