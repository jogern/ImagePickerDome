package com.studyhelper.uilib.ui.fragment;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.studyhelper.uilib.ui.BaseFragActivity;

/**
 * Create on 2019-08-13.
 *
 * @author zujianliang
 */
public class BaseFragFragment extends BaseFragment {

    /** 增加Fragment到栈中 */
    protected void addFragmentInStack(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseFragActivity){
            ((BaseFragActivity) activity).addFragmentInStack(fragment);
        }
    }

    /** 增加Fragment到栈中,有动画效果 */
    protected void addFragmentInStack(int enter, int exit, int popEnter, int popExit, Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseFragActivity){
            ((BaseFragActivity) activity).addFragmentInStack(enter, exit, popEnter, popExit, fragment);
        }
        // getBaseActivity().addFragmentInStack(enter, exit, popEnter, popExit, fragment);
    }

    /** 增加Fragment到容器中 */
    protected void addFragmentInContainer(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseFragActivity){
            ((BaseFragActivity) activity).addFragmentInContainer(fragment);
        }
        //getBaseActivity().addFragmentInContainer(fragment);
    }

    /**
     * 增加Fragment到容器中,有动画效果
     * <br/>
     * 请使用.setCustomAnimations(enter, exit, popEnter, popExit)，
     * 这个方法的第1个参数对应进栈动画，第4个参数对应出栈动画，<br/>
     * 所以是.setCustomAnimations(进栈动画, exit, popEnter, 出栈动画))
     */
    protected void addFragmentInContainer(int enter, int exit, int popEnter, int popExit, Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseFragActivity){
            ((BaseFragActivity) activity).addFragmentInContainer(enter, exit, popEnter, popExit, fragment);
        }
//            getBaseActivity().addFragmentInContainer(enter, exit, popEnter, popExit, fragment);
    }

    /** 移除栈顶的Fragment */
    protected void removePopFragmentOfStack() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseFragActivity){
            ((BaseFragActivity) activity).removePopFragmentOfStack();
        }
//            getBaseActivity().removePopFragmentOfStack();
    }

}
