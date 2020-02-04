package com.heady.ui.home

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
import com.heady.db.CategoriesDAO
import com.heady.db.entity.CategoriesItem
import com.heady.db.entity.ProductsItem
import com.heady.db.entity.RankingsItem
import com.heady.network.Resource
import com.heady.ui.viewmodel.CommonViewModel
import com.heady.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeFragment() : DaggerFragment(),
    RankingListAdapter.Interaction {

    lateinit var dao: CategoriesDAO

    @Inject
    constructor(categoryDAO: CategoriesDAO) : this() {
        dao = categoryDAO
    }

    override fun onItemSelected(position: Int, item: RankingsItem) {
    }

    private val disposable = CompositeDisposable()

    private lateinit var commonViewModel: CommonViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainRecyclerAdapter: RankingListAdapter

    private val daoDisposable = CompositeDisposable()

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
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = root.findViewById(R.id.recycler_home) as RecyclerView

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeObservers()
        commonViewModel.callApi()
        commonViewModel.checkLocalRanking()
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            mainRecyclerAdapter = RankingListAdapter(this@HomeFragment)
            adapter = mainRecyclerAdapter
        }
    }

    private fun subscribeObservers() {
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
                                var listAllProducts = mutableListOf<ProductsItem>()
                                for (eachCategory in numberRest) {
                                    eachCategory.products?.let { listAllProducts.addAll(it) }
                                }

                                insertCategories(response.data.categories!!)
                                insertRankings(response.data.rankings!!)
                            }
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Log.e("ERROR", "onChanged: ERROR..." + response.message)
                }
            }
        })

        commonViewModel.rankings.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    // show progress
                }
                Resource.Status.SUCCESS -> {
                    if (response.data != null) {
                        val list = response.data
                        if (list.isNotEmpty()) {
                            commonViewModel.updateProductListHome(list,commonViewModel.listProductItems.value!!)
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    Log.e("ERROR", "onChanged: ERROR..." + response.message)
                }
            }
        })

        commonViewModel.rankingsProducts.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    // show progress
                }
                Resource.Status.SUCCESS -> {
                    if (response.data != null) {
                        val list = response.data
                        if (list.isNotEmpty()) {
                            mainRecyclerAdapter.submitList(commonViewModel.listRankingItems.value!!, list)
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    Log.e("ERROR", "onChanged: ERROR..." + response.message)
                }
            }
        })
    }

    private fun insertCategories(categoriesItem: List<CategoriesItem>) {
        disposable.add(
            commonViewModel.insertCategories(categoriesItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        print("Inserted !!!!!!!!!!!!!!!")
                    },
                    { error -> Log.e("Error !!!!!!!", "Unable to update username", error) })
        )
    }

    private fun insertRankings(rankingsItem: List<RankingsItem>) {
        disposable.add(
            commonViewModel.insertRanking(rankingsItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        print("Inserted !!!!!!!!!!!!!!!")
                    },
                    { error -> Log.e("Error !!!!!!!", "Unable to update username", error) })
        )
    }


    override fun onStop() {
        super.onStop()
        disposable.dispose()
        daoDisposable.dispose()
    }


}