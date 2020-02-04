package com.heady.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heady.db.CategoriesDAO
import com.heady.db.entity.CategoriesItem
import com.heady.db.entity.ProductsItem
import com.heady.db.entity.RankingsItem
import com.heady.db.entity.Shopping
import com.heady.network.MainApi
import com.heady.network.Resource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CommonViewModel @Inject constructor(categoryDAO: CategoriesDAO, mainApi: MainApi) :
    ViewModel() {

    var listRankingItems: MutableLiveData<List<RankingsItem>> = MutableLiveData()
    var listProductItems: MutableLiveData<List<ProductsItem>> = MutableLiveData()

    private val api = mainApi
    private val dao = categoryDAO

    var response: MutableLiveData<Resource<Shopping>> = MutableLiveData()
    var categories: MutableLiveData<Resource<List<CategoriesItem>>> = MutableLiveData()

    var rankings: MutableLiveData<Resource<List<RankingsItem>>> = MutableLiveData()
    var rankingsProducts: MutableLiveData<Resource<List<ProductsItem>>> = MutableLiveData()

    private var searchDisposable: Disposable? = null
    private val disposable = CompositeDisposable()
    private val daoDisposable = CompositeDisposable()


    fun callApi() {
        categories.setValue(Resource.loading(null)) // local
        disposable.add(
            dao.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listCategories ->
                    if (listCategories != null && listCategories.isNotEmpty()) {

                        var listAllProducts = mutableListOf<ProductsItem>()
                        for (eachCategory in listCategories) {
                            eachCategory.products?.let { listAllProducts.addAll(it) }
                        }
                        listProductItems.value = listAllProducts

                        categories.value = Resource.success(listCategories)



                    } else {
                        response.setValue(Resource.loading(null))
                        searchDisposable = api.getList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ searchResponse ->
                                if (searchResponse != null) {

                                    var listAllProducts = mutableListOf<ProductsItem>()
                                    for (eachCategory in searchResponse.categories!!) {
                                        eachCategory.products?.let { listAllProducts.addAll(it) }
                                    }
                                    listProductItems.value = listAllProducts

                                    response.value = Resource.success(searchResponse)
                                }
                            }, { throwable ->
                                response.setValue(Resource.error("Something went wrong!!!", null))
                            })
                    }
                }, { throwable ->
                    categories.setValue(Resource.error("Something went wrong local!!!", null))
                })
        )

    }

    fun checkLocalRanking() {
        rankings.value = (Resource.loading(null))
        disposable.add(
            dao.getRankingList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rankingList ->
                    if (rankingList != null && rankingList.isNotEmpty()) {

                        listRankingItems.value = rankingList

                        rankings.value = Resource.success(rankingList)
                    } else {
                        response.setValue(Resource.loading(null))
                        searchDisposable = api.getList()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ searchResponse ->
                                if (searchResponse != null) {

                                    listRankingItems.value = searchResponse.rankings!!

                                    response.value = Resource.success(searchResponse)
                                }
                            }, { throwable ->
                                response.setValue(Resource.error("Something went wrong!!!", null))
                            })
                    }

                }, { throwable ->
                    rankings.setValue(Resource.error("Something went wrong local!!!", null))
                })
        )
    }

    fun updateProductListHome(list: List<RankingsItem>, listProducts: List<ProductsItem>) {
        var products: MutableList<ProductsItem> = mutableListOf()
        rankingsProducts.value = Resource.loading(null)

        for (eachRankItem in list) {
            for (product in eachRankItem.products!!) {
                for (eachProduct in listProducts) {
                    if (eachProduct.id == product.id) {
                        products.add(eachProduct)
                    }
                }
            }
        }
        if (products.size > 0)
            rankingsProducts.value = Resource.success(products)
    }


    fun insertCategories(categoriesItem: List<CategoriesItem>): Completable {
        return dao.insertAll(categoriesItem)
    }

    fun insertRanking(rankingsItem: List<RankingsItem>): Completable {
        return dao.insertAllRanking(rankingsItem)
    }


    override fun onCleared() {
        super.onCleared()
        searchDisposable?.dispose()
        disposable.dispose()
        daoDisposable.dispose()
    }
}