package com.studyhelper.uilib.ui;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;

/**
 * Create on 2019-08-13.
 *
 * @author zujianliang
 */
public abstract class BaseFragActivity extends BaseActivity {

    private int mFragmentContainerId;
    private WeakReference<Fragment> mContainerFragment;

    /**
     * 设置 Fragment 容器的 ResId
     *
     * @param fragmentContainerId
     */
    protected void setFragmentContainerId(int fragmentContainerId) {
        mFragmentContainerId = fragmentContainerId;
    }

    /**
     * 增加Fragment到栈中
     */
    public void addFragmentInStack(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);*/
            String tag = fragment.getClass().getName();
            ft.replace(mFragmentContainerId, fragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        }
    }

    /**
     * 增加Fragment到栈中,有动画效果
     * <br/>
     * 请使用.setCustomAnimations(enter, exit, popEnter, popExit)，
     * 这个方法的第1个参数对应进栈动画，第4个参数对应出栈动画，<br/>
     * 所以是.setCustomAnimations(进栈动画, exit, popEnter, 出栈动画))
     */
    public void addFragmentInStack(int enter, int exit, int popEnter, int popExit, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(enter, exit, popEnter, popExit);
            String tag = fragment.getClass().getName();
            ft.replace(mFragmentContainerId, fragment, tag)
                    //将Fragment添加到回退栈中
                    .addToBackStack(tag).commitAllowingStateLoss();
        }
    }

    /**
     * 增加Fragment到容器中
     */
    public void addFragmentInContainer(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);*/
            String tag = fragment.getClass().getName();
            mContainerFragment = new WeakReference<>(fragment);
            ft.replace(mFragmentContainerId, fragment, tag).commitAllowingStateLoss();
        }
    }

    /**
     * 增加Fragment到容器中,有动画效果
     * <br/>
     * 请使用.setCustomAnimations(enter, exit, popEnter, popExit)，
     * 这个方法的第1个参数对应进栈动画，第4个参数对应出栈动画，<br/>
     * 所以是.setCustomAnimations(进栈动画, exit, popEnter, 出栈动画))
     */
    public void addFragmentInContainer(int enter, int exit, int popEnter, int popExit, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(enter, exit, popEnter, popExit);
            String tag = fragment.getClass().getName();
            mContainerFragment = new WeakReference<>(fragment);
            ft.replace(mFragmentContainerId, fragment, tag).commitAllowingStateLoss();
        }
    }

    /**
     * 移除栈顶的Fragment
     */
    public void removePopFragmentOfStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int entryCount = fragmentManager.getBackStackEntryCount();
        if (entryCount > 1) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * 获取栈顶的Fragment
     */
    public Fragment getPopFragmentOfStack() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(count - 1);

            return manager.findFragmentByTag(entry.getName());
        }
        return null;

//            List<Fragment> fragments = manager.getFragments();
//            if (fragments != null && fragments.size() > 0) {
//                  return fragments.get(fragments.size() - 1);
//            }

    }

    /**
     * 获取 Fragment 栈中的 Fragment 的数量
     *
     * @return
     */
    protected int getBackStackEntryCount() {
        FragmentManager manager = getSupportFragmentManager();
        return manager.getBackStackEntryCount();
    }

    /**
     * 移除已经增加的 Fragment
     * @param fragment
     */
    public void removeAddedFragment(Fragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 获取增加到容器中(没有加入栈)的Fragment
     *
     * @return
     */
    @Nullable
    public Fragment getContainerFragment() {
        if (mContainerFragment != null)
            return mContainerFragment.get();

        return null;
    }


//      @Override
//      public void onBackPressed() {
//            Fragment popFragment = getPopFragmentOfStack();
//            if (popFragment instanceof BaseInitialFragment) {
//                  if (((BaseInitialFragment) popFragment).onBackPressed()) {
//                        return;
//                  }
//            }
//            FragmentManager manager = getSupportFragmentManager();
//            int entryCount = manager.getBackStackEntryCount();
//            if (entryCount > 1) {
//                  super.onBackPressed();
//            } else {
//                  finish();
//            }
//      }

}
