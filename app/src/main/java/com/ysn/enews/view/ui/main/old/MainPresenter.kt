package com.ysn.enews.view.ui.main.old

import com.ysn.enews.view.base.mvp.MvpPresenter

/**
 * Created by yudisetiawan on 2/19/18.
 */
class MainPresenter : MvpPresenter<MainView> {

    private val TAG = javaClass.simpleName
    private var mainView: MainView? = null

    override fun onAttach(mvpView: MainView) {
        mainView = mvpView
    }

    override fun onDetach() {
        mainView = null
    }

}