package com.tailor.app.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.transition.TransitionInflater

import com.tailor.app.R
import com.tailor.app.base.BaseActivity
import com.tailor.app.base.BaseFragment
import javax.inject.Singleton

@Singleton
class FragmentNavigationFactory constructor(val context: Context) {

    private var fragment: BaseFragment<*, *>? = null
    private var tag: String? = null

    fun <T : BaseFragment<*, *>> make(aClass: Class<T>): FragmentActionPerformer<T> {
        return make(FragmentFactory.getFragment(aClass))
    }

    fun <T : BaseFragment<*, *>> make(fragment: T?): FragmentActionPerformer<T> {
        this.fragment = fragment
        this.tag = fragment!!.javaClass.simpleName
        return Provider(fragment)
    }

    private inner class Provider<T : BaseFragment<*, *>>(private val fragment: T) :
        FragmentActionPerformer<T> {
        internal var sharedElements: List<Pair<View, String>>? = null

        override fun add(toBackStack: Boolean) {
            openFragment(
                fragment,
                Option.ADD, toBackStack, tag!!, sharedElements
            )
        }

        override fun add(toBackStack: Boolean, tag: String) {
            openFragment(
                fragment,
                Option.ADD, toBackStack, tag, sharedElements
            )
        }

        override fun replace(toBackStack: Boolean) {
            openFragment(
                fragment,
                Option.REPLACE, toBackStack, tag!!, sharedElements
            )
        }

        override fun replace(toBackStack: Boolean, tag: String) {
            openFragment(
                fragment,
                Option.REPLACE, toBackStack, tag, sharedElements
            )
        }


        override fun setBundle(bundle: Bundle): FragmentActionPerformer<*> {
            fragment.arguments = bundle
            return this
        }

        override fun addSharedElements(sharedElements: List<Pair<View, String>>): FragmentActionPerformer<*> {
            this.sharedElements = sharedElements
            return this
        }

        override fun clearHistory(tag: String?): FragmentActionPerformer<*> {
            clearFragmentHistory(tag)
            return this
        }

        override fun hasData(passable: Passable<T>): FragmentActionPerformer<*> {
            passable.passData(fragment)
            return this
        }
    }


    private var intent: Intent? = null
    private var activity: Class<out Activity>? = null
    private var shouldAnimate = true

    fun make(activityClass: Class<out BaseActivity<*, *>>): ActivityBuilder {
        activity = activityClass
        intent = Intent(context, activityClass)
        return Builder()
    }

    private inner class Builder : ActivityBuilder {
        private var bundle: Bundle? = null
        private var activityOptionsBundle: Bundle? = null
        private var isToFinishCurrent: Boolean = false
        private var requestCode: Int = 0

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun start() {
            if (bundle != null)
                intent!!.putExtras(bundle!!)

            if (!shouldAnimate)
                intent!!.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            if (requestCode == 0) {

                if (activityOptionsBundle == null)
                    context.startActivity(intent)
                else
                    context.startActivity(intent, activityOptionsBundle)

            } else {
                val currentFragment = FragmentFactory.getCurrentFragment<BaseFragment<*, *>>(context)
                if (currentFragment != null)
                    currentFragment.startActivityForResult(intent, requestCode)
                else
                    (context as BaseActivity<*, *>).startActivityForResult(intent, requestCode)
            }


            if (isToFinishCurrent)
                (context as BaseActivity<*, *>).finish()
        }

        override fun addBundle(bundle: Bundle): ActivityBuilder {
            if (this.bundle != null)
                this.bundle!!.putAll(bundle)
            else
                this.bundle = bundle
            return this
        }

        override fun addSharedElements(pairs: List<Pair<View, String>>): ActivityBuilder {
            val optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(context as BaseActivity<*, *>, *pairs.toTypedArray())
            activityOptionsBundle = optionsCompat.toBundle()
            return this
        }

        override fun byFinishingCurrent(): ActivityBuilder {
            isToFinishCurrent = true
            return this
        }

        override fun byFinishingAll(): ActivityBuilder {
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            return this
        }


        override fun <T : BaseFragment<*, *>> setPage(page: Class<T>): ActivityBuilder {
            intent!!.putExtra(ACTIVITY_FIRST_PAGE, page)
            return this
        }

        override fun forResult(requestCode: Int): ActivityBuilder {
            this.requestCode = requestCode
            return this
        }

        override fun shouldAnimate(isAnimate: Boolean): ActivityBuilder {
            shouldAnimate = isAnimate
            return this
        }
    }


    fun openFragment(
        baseFragment: BaseFragment<*, *>,
        option: Option,
        isToBackStack: Boolean,
        tag: String,
        sharedElements: List<Pair<View, String>>?
    ) {
        val fragmentTransaction = (context as BaseActivity<*, *>).supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.down_to_up,
            R.anim.up_to_down,
            R.anim.down_to_up,
            R.anim.up_to_down
        )

        when (option) {

            Option.ADD -> fragmentTransaction.add(FragmentFactory.findFragmentPlaceHolder(), baseFragment, tag)
            Option.REPLACE -> fragmentTransaction.replace(FragmentFactory.findFragmentPlaceHolder(), baseFragment, tag)
            Option.SHOW -> if (baseFragment.isAdded)
                fragmentTransaction.show(baseFragment)
            Option.HIDE -> if (baseFragment.isAdded)
                fragmentTransaction.hide(baseFragment)
        }

        if (isToBackStack)
            fragmentTransaction.addToBackStack(tag)

        // shared element Transition
        if (sharedElements != null
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            && sharedElements.isNotEmpty()
        ) {


            val mBaseFragment =
                context.supportFragmentManager.findFragmentById(FragmentFactory.findFragmentPlaceHolder())

            val changeTransform =
                TransitionInflater.from(mBaseFragment?.context).inflateTransition(R.transition.change_transform)


            mBaseFragment?.sharedElementReturnTransition = changeTransform
            // currentFragment.setExitTransition(new Fade());

            mBaseFragment?.sharedElementEnterTransition = changeTransform
            //baseFragment.setEnterTransition(new Fade());


            for (se in sharedElements) {
                fragmentTransaction.addSharedElement(se.first!!, se.second!!)
            }
        }

        fragmentTransaction.commitAllowingStateLoss()
    }


    fun clearFragmentHistory(tag: String?) {
        sDisableFragmentAnimations = true
        (context as BaseActivity<*, *>).supportFragmentManager.popBackStackImmediate(tag, POP_BACK_STACK_INCLUSIVE)
        sDisableFragmentAnimations = false
    }

    companion object {
        const val ACTIVITY_FIRST_PAGE = "first_page"
        var sDisableFragmentAnimations = false
    }

    enum class Option {
        ADD, REPLACE, SHOW, HIDE
    }
}