package com.isoftinc.film.util


import android.transition.Transition
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.isoftinc.film.R


open class BaseActivity : AppCompatActivity() {

    fun replaceFragment(fragment: BaseFragment) {
        replaceFragment(fragment, false, true, false)
    }

    fun replaceFragment(fragment: BaseFragment, isAdd: Boolean) {
        replaceFragment(fragment, isAdd, true, false)
    }


    fun replaceFragment(fragment: BaseFragment, isAdd: Boolean, addtobs: Boolean) {
        replaceFragment(fragment, isAdd, addtobs, false)
    }

    fun replaceFragment(
        fragment: BaseFragment,
        isAdd: Boolean,
        addtobs: Boolean,
        forceWithoutAnimation: Boolean
    ) {
        replaceFragment(fragment, isAdd, addtobs, forceWithoutAnimation, null)
    }




   open fun replaceFragment(
        fragment: BaseFragment?,
        isAdd: Boolean,
        addtobs: Boolean,
        forceWithoutAnimation: Boolean,
        transition: Transition?
    ) {
        //to do in child activity


        if (fragment == null)
            return
        val ft = supportFragmentManager.beginTransaction()
        val tag = fragment.javaClass.simpleName
        ft.setReorderingAllowed(true)
        if (!forceWithoutAnimation) {
            if (AndroidUtilities().isAndroid5()) {
                if (transition != null) {
                    fragment.enterTransition = transition
                } else {
                    fragment.enterTransition = TransitionUtil().slide(Gravity.RIGHT)
                }
            } else {
                ft.setCustomAnimations(
                    R.anim.pull_in_right,
                    R.anim.push_out_left,
                    R.anim.pull_in_left,
                    R.anim.push_out_right
                )
            }
        }
        if (isAdd)
            ft.add(android.R.id.content, fragment, tag)
        else
            ft.replace(android.R.id.content, fragment, tag)
        if (addtobs)
            ft.addToBackStack(tag)
        ft.commitAllowingStateLoss()

    }

   open override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            getCurrentFragment()!!.onBackPressed()
        } else
            finish()

    }


    open fun getCurrentFragment(): BaseFragment? {
        return supportFragmentManager.findFragmentById(android.R.id.content) as BaseFragment
    }

    fun removeFragments(no: Int) {
        try {

            val fragmentManager = supportFragmentManager
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(
                fragmentManager.backStackEntryCount - no).id, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    fun clearAllStack()
    {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

}
