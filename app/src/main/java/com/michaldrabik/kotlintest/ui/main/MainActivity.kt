package com.michaldrabik.kotlintest.ui.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.michaldrabik.kotlintest.R
import com.michaldrabik.kotlintest.data.models.Joke
import com.michaldrabik.kotlintest.ui.base.BaseActivity
import com.michaldrabik.kotlintest.utilities.DividerItemDecoration
import com.michaldrabik.kotlintest.utilities.dpToPx
import com.michaldrabik.kotlintest.utilities.hide
import com.michaldrabik.kotlintest.utilities.show
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : BaseActivity(), MainView, SwipeRefreshLayout.OnRefreshListener {

  val adapter = MainAdapter()
  val presenter = MainPresenter()

  override fun getLayoutResId() = R.layout.activity_main

  override fun getActivityTitle(): String = getString(R.string.chuck_norris_jokes)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    presenter.bind(this)
    setupRecycler()
    setupSwipeToRefresh()
    fetchJokes()
  }

  private fun setupRecycler() {
    recyclerView.setHasFixedSize(true)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 8.dpToPx(this).toInt()))
    recyclerView.adapter = adapter
  }

  private fun setupSwipeToRefresh() {
    swipeRefreshLayout.setOnRefreshListener(this)
  }

  private fun fetchJokes() {
    presenter.fetchJokes()
    progressBar.show()
  }

  override fun onFetchJokesSuccess(jokes: List<Joke>) {
    adapter.clearItems()
    adapter.addItems(jokes)
    progressBar.hide()
    swipeRefreshLayout.isRefreshing = false;
  }

  override fun onFetchJokesError(error: Throwable) {
    Toast.makeText(this, R.string.something_went_wrong_error, Toast.LENGTH_SHORT).show()
    progressBar.hide()
    swipeRefreshLayout.isRefreshing = false;
  }

  override fun onRefresh() {
    presenter.fetchJokes()
  }

  override fun onDestroy() {
    presenter.onDestroy()
    super.onDestroy()
  }

}
