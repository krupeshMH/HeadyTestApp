package com.heady.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.heady.R
import com.heady.db.entity.ProductsItem
import com.heady.db.entity.RankingsItem
import com.heady.ui.adapter.ProductListAdapter
import kotlinx.android.synthetic.main.layout_categoty_item.view.*

class RankingListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val products: MutableList<ProductsItem> = mutableListOf()
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RankingsItem>() {

        override fun areItemsTheSame(oldItem: RankingsItem, newItem: RankingsItem): Boolean {
            return oldItem.ranking == newItem.ranking
        }

        override fun areContentsTheSame(oldItem: RankingsItem, newItem: RankingsItem): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RankingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_categoty_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RankingItemViewHolder -> {
                holder.bind(differ.currentList.get(position), products)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<RankingsItem>, listProducts: List<ProductsItem>) {
        products.addAll(listProducts)
        differ.submitList(list)
    }

    class RankingItemViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: RankingsItem, products: List<ProductsItem>) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            val recyclerView: RecyclerView = itemView.rv_products
            val textView: TextView = itemView.tv
            textView.text = item.ranking
            val productAdapter: ProductListAdapter

            recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(recyclerView.context, LinearLayout.HORIZONTAL, false)

                productAdapter = ProductListAdapter()
                adapter = productAdapter
            }

            productAdapter.submitList(products)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: RankingsItem)
    }
}
