package com.heady.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heady.R
import com.heady.db.entity.CategoriesItem
import com.heady.db.entity.RankingsItem
import com.heady.network.Resource
import com.heady.ui.viewmodel.CommonViewModel
import com.heady.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoriesFragment : DaggerFragment(), CategoryListAdapter.Interaction {
    override fun onItemSelected(position: Int, item: CategoriesItem) {

    }

    private lateinit var commonViewModel: CommonViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainRecyclerAdapter: CategoryListAdapter

    @Inject
    @JvmField
    internal var providerFactory: ViewModelProviderFactory? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        commonViewModel =
            ViewModelProviders.of(this, providerFactory).get(CommonViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_categories, container, false)
        recyclerView = root.findViewById(R.id.recycler_categories) as RecyclerView
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeObservers()
        commonViewModel.callApi()
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            mainRecyclerAdapter = CategoryListAdapter(this@CategoriesFragment)
            adapter = mainRecyclerAdapter
        }
    }

    private fun subscribeObservers() {
        //api
        commonViewModel.response.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    // show progress
                }
                Resource.Status.SUCCESS -> {
                    if (response.data?.categories != null) {
                        val numberRest = response.data.categories
                        if (numberRest != null) {
                            if (numberRest.isNotEmpty()) {
                                mainRecyclerAdapter.submitList(response.data.categories!!)
                                //insertCategories(response.data.categories!!)
                                //insertRankings(response.data.rankings!!)
                            }
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Log.e("ERROR", "onChanged: ERROR..." + response.message)
                }
            }
        })

        // local
        commonViewModel.categories.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    // show progress
                }
                Resource.Status.SUCCESS -> {
                    if (response.data != null) {
                        val numberRest = response.data
                        if (numberRest != null) {
                            if (numberRest.isNotEmpty()) {
                                mainRecyclerAdapter.submitList(response.data)
                            }
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Log.e("ERROR", "onChanged: ERROR..." + response.message)
                }
            }
        })
    }

}