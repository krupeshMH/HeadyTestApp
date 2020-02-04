package com.heady.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.heady.R
import com.heady.db.entity.ProductsItem
import kotlinx.android.synthetic.main.layout_item_product.view.*

class ProductListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsItem>() {

        override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_item_product,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProductsItem>) {
        differ.submitList(list)
    }

    class ProductViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProductsItem) = with(itemView) {
            itemView.setOnClickListener {
            }
            val tvName: TextView = itemView.txt_name
            val tvVariant: TextView = itemView.txt_variants
            tvName.text = item.name
            if (item.variants != null && item.variants!!.isNotEmpty()) {
                tvVariant.visibility = View.VISIBLE
                var finalText: String = "Variants :\n"
                for (eachVariant in item.variants!!) {
                    finalText += "\n" + eachVariant.color
                }
                tvVariant.text = finalText
            } else {
                tvVariant.visibility = View.GONE
            }
        }
    }

}
