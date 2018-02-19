package com.ysn.enews.view.base.mvp

/**
 * Created by yudisetiawan on 2/19/18.
 */
interface MvpPresenter<in T : MvpView> {

    fun onAttach(mvpView: T)

    fun onDetach()

}