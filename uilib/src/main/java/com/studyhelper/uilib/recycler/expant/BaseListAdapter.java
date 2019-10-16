package com.studyhelper.uilib.recycler.expant;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Create on 2019/3/28.
 * @author jogern
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

      protected List<T> mDataList;

      protected void setDataList(List<T> dataList) {
            mDataList = dataList;
      }

      public List<T> getDataList() {
            return mDataList;
      }

      @Override
      public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
      }

      @Override
      public T getItem(int position) {
            if (position >= 0 && position < getCount()) {
                  return mDataList.get(position);
            }
            return null;
      }

      @Override
      public long getItemId(int position) {
            return 0;
      }

}
