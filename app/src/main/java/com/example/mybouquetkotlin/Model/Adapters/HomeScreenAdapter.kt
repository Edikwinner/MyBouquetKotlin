package com.example.mybouquetkotlin.Model.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.ViewModel.Fragments.HomeViewModel
import com.squareup.picasso.Picasso


class HomeScreenAdapter(private val cards: List<Card>, private val listener: customListener, val viewModel: HomeViewModel) :
    RecyclerView.Adapter<HomeScreenAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_home_screen_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card, listener, viewModel)

    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card_name: TextView = view.findViewById(R.id.bouquet_name_recycler_view_item)
        var card_image: ImageView
        var add_to_cart: Button
        var add_to_favourite: ImageButton

        init {
            card_image = view.findViewById(R.id.image_view_recycler_view_item)
            add_to_cart = view.findViewById(R.id.bouquet_cost_recycler_view_item)
            add_to_favourite = view.findViewById(R.id.add_to_favourite)
        }
        fun bind(card: Card, listener: customListener?, viewModel: HomeViewModel){
            card_name.text = card.bouquetName
            add_to_cart.text = card.bouquetCost.toString() + " ₽"

            if(viewModel.favouriteCards.value?.contains(card) == true){
                add_to_favourite.setImageResource(R.drawable.favourite_clicked)
            }
            if(viewModel.shoppingCards.value?.contains(card) == true){
                add_to_cart.text = "Удалить"
            }

            Picasso.get().load(Uri.parse(card.bouquetImage)).into(card_image)
            add_to_favourite.setOnClickListener{
                listener!!.onFavouriteButtonClicked(card, add_to_favourite)
            }
            add_to_cart.setOnClickListener {
                listener!!.onAddToShoppingCartClicked(card, add_to_cart)
            }
            itemView.setOnClickListener{
                listener!!.onCardViewClicked(card)
            }
        }
    }

    interface customListener {
        fun onFavouriteButtonClicked(card: Card, button: ImageButton)
        fun onAddToShoppingCartClicked(card: Card, button: Button)
        fun onCardViewClicked(card: Card)
    }
}