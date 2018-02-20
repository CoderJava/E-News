package com.ysn.enews.view.ui.main.old

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.ysn.e_news.R
import com.ysn.enews.view.ui.main.adapter.MainAdapterBak
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    private val TAG = javaClass.simpleName
    private var mainPresenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPresenter()
        onAttachView()
        doLoadData()
    }

    private fun doLoadData() {
        val listData = ArrayList<String>()
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")
        listData.add("data")

        val listViewType = ArrayList<Int>()
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_HEADLINE)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)
        listViewType.add(MainAdapterBak.VIEW_TYPE_NEWS)

        recycler_view_activity_main.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recycler_view_activity_main.addItemDecoration(dividerItemDecoration)

        val mainAdapter = MainAdapterBak(this, listData, listViewType)
        recycler_view_activity_main.adapter = mainAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    private fun initPresenter() {
        mainPresenter = MainPresenter()
    }

    override fun onAttachView() {
        mainPresenter?.onAttach(this)
    }

    override fun onDetachView() {
        mainPresenter?.onDetach()
    }

}
