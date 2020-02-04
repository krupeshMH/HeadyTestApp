package com.heady.ui.category

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
import com.heady.db.entity.CategoriesItem
import com.heady.ui.adapter.ProductListAdapter
import kotlinx.android.synthetic.main.layout_categoty_item.view.*


class CategoryListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val viewPool = RecyclerView.RecycledViewPool()
    private var  productAdapter = ProductListAdapter()

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoriesItem>() {

        override fun areItemsTheSame(oldItem: CategoriesItem, newItem: CategoriesItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoriesItem, newItem: CategoriesItem): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CategoryViewHolder(
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
            is CategoryViewHolder -> {
                holder.bind(differ.currentList.get(position))

            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CategoriesItem>) {
        differ.submitList(list)
    }

    class CategoryViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CategoriesItem) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            val recyclerView: RecyclerView = itemView.rv_products
            val textView: TextView = itemView.tv
            textView.text = item.name
            val productAdapter: ProductListAdapter
            /*recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
                productAdapter = ProductListAdapter()
                productAdapter.submitList(item.products!!)
            }*/
            recyclerView.apply {
                layoutManager = LinearLayoutManager(recyclerView.context, LinearLayout.HORIZONTAL, false)

                productAdapter = ProductListAdapter()
                adapter = productAdapter
            }
            productAdapter.submitList(item.products!!)


        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CategoriesItem)
    }

}
