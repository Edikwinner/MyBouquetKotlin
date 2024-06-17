package com.example.mybouquetkotlin.Model.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.R
import com.squareup.picasso.Picasso

class ShoppingCartScreenAdapter(private val cards: List<Card?>?) :
    RecyclerView.Adapter<ShoppingCartScreenAdapter.ViewHolder>() {
    private var listener: customOnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.recycler_view_shopping_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards!!.get(position))
    }


    override fun getItemCount(): Int {
        return cards!!.size
    }

    fun setListener(listener: customOnClickListener?) {
        this.listener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card_name: TextView
        var card_image: ImageView
        var card_cost: TextView

        init {
            card_name = view.findViewById(R.id.bouquet_name_shopping_cart_item)
            card_image = view.findViewById(R.id.bouquet_image_shopping_cart_item)
            card_cost = view.findViewById(R.id.bouquet_cost_shopping_cart_item)
        }

        fun bind(card: Card?) {
            card_name.setText(card!!.bouquetName)
            card_cost.setText(card.bouquetCost.toString() + " ₽")
            Picasso.get().load(Uri.parse(card.bouquetImage)).into(card_image)
        }
    }

    interface customOnClickListener {
        fun deleteCard(card: Card?)
    }
}