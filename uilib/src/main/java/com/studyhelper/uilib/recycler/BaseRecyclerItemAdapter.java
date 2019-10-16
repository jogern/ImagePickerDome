package com.studyhelper.uilib.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 生命周期 <br/>
 * 一个RecyclerView的Item加载是有顺序的，类似于Activity的生命周期（姑且这么叫），<br/>
 * 具体可以对adapter的每个方法进行重写打下日志进行查看，具体大致为：<br/>
 * <br/>
 * getItemViewType(获取显示类型，返回值可在onCreateViewHolder中拿到，以决定加载哪种ViewHolder)<br/>
 * <br/>
 * onCreateViewHolder(加载ViewHolder的布局)<br/>
 * <br/>
 * onViewAttachedToWindow（当Item进入这个页面的时候调用）<br/>
 * <br/>
 * onBindViewHolder(将数据绑定到布局上，以及一些逻辑的控制就写这啦) <br/>
 * <br/>
 * onViewDetachedFromWindow（当Item离开这个页面的时候调用）<br/>
 * <br/>
 * onViewRecycled(当Item被回收的时候调用) <br/>
 *
 * Create on 17-9-14.
 * @author jogern
 */
 
public abstract class BaseRecyclerItemAdapter extends RecyclerView.Adapter<RecyclerHolder> {
 
      /**
       * 返回位置索引的Item的View的类型
       * @param position 位置索引
       * @return View的类型
       */
      @Override
      public abstract int getItemViewType(int position);
 
      /**
       * Item的View的类型得到ViewHolder
       * @param viewType View的类型
       * @return RecyclerViewHolder
       */
      protected abstract BaseRecyclerViewHolder getViewHolder(int viewType);
 
      @Override
      public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BaseRecyclerViewHolder viewHolder = getViewHolder(viewType);
            int layout = viewHolder.getLayout();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(layout, parent, false);
            return new RecyclerHolder(view,this, viewHolder);
      }
 
      @Override
      public final void onBindViewHolder(RecyclerHolder holder, int position) {
            onBindViewHolder(holder, position,null);
      }
 

      @Override
      public final void onBindViewHolder(RecyclerHolder holder, int position, List<Object> payloads) {
            holder.onBinder(position, payloads);
      }
 
      @Override
      public final void onViewRecycled(RecyclerHolder holder) {
            holder.onViewRecycled();
      }
}