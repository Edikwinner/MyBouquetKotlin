package com.example.mybouquetkotlin.recycler_view_home_screen

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.data.Card
import com.squareup.picasso.Picasso


class HomeScreenAdapter(private val cards: List<Card?>?) :
    RecyclerView.Adapter<HomeScreenAdapter.ViewHolder>() {
    private var listener: customOnClickListener? = null
    private var favouriteCards: List<Card?>? = null
    private var shoppingCards: List<Card?>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.recycler_view_home_screen_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards!!.get(position), listener, favouriteCards, shoppingCards)
    }

    override fun getItemCount(): Int {
        return cards!!.size
    }

    fun setOnClickListener(listener: customOnClickListener?) {
        this.listener = listener
    }

    fun setFavouriteCards(favouriteCards: List<Card?>?) {
        this.favouriteCards = favouriteCards
    }

    fun setShoppingCards(shoppingCards: List<Card?>?) {
        this.shoppingCards = shoppingCards
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card_name: TextView
        var card_image: ImageView
        var add_to_cart: Button
        var add_to_cart_false: Button
        var add_to_favourite: ImageButton

        init {
            card_name = view.findViewById(R.id.bouquet_name_recycler_view_item)
            card_image = view.findViewById(R.id.image_view_recycler_view_item)
            add_to_cart = view.findViewById(R.id.bouquet_cost_recycler_view_item)
            add_to_cart_false = view.findViewById(R.id.bouquet_cost_recycler_view_item_false)
            add_to_favourite = view.findViewById(R.id.add_to_favourite)
        }

        fun bind(
            card: Card?,
            listener: customOnClickListener?,
            favCards: List<Card?>?,
            shopCards: List<Card?>?
        ) {
            var isLiked: Boolean = false
            var isShopped: Boolean = false
            card_name.setText(card!!.bouquetName)
            add_to_cart.setText(card!!.bouquetCost.toString() + " ₽")
            for (card1: Card? in favCards!!) {
                if ((card1!!.path == card.path)) {
                    isLiked = true
                    break
                }
            }
            for (card1: Card? in shopCards!!) {
                if ((card1!!.path == card.path)) {
                    isShopped = true
                    break
                }
            }
            if (isLiked) {
                add_to_favourite.setImageResource(R.drawable.favourite_clicked)
            }
            if (isShopped) {
                add_to_cart.setVisibility(View.GONE)
                add_to_cart_false.setVisibility(View.VISIBLE)
            }
            Picasso.get().load(Uri.parse(card.bouquetImage)).into(card_image)
            itemView.setOnClickListener(View.OnClickListener({ v: View? ->
                listener!!.onCardViewClicked(
                    card
                )
            }))
            add_to_favourite.setOnClickListener(View.OnClickListener({ v: View? ->
                listener!!.onFavouriteButtonClicked(
                    card,
                    add_to_favourite
                )
            }))
            add_to_cart.setOnClickListener(View.OnClickListener({ v: View? ->
                listener!!.onAddToShoppingCartClicked(
                    card,
                    add_to_cart,
                    add_to_cart_false
                )
            }))
            add_to_cart_false.setOnClickListener(View.OnClickListener({ v: View? ->
                listener!!.onAddToShoppingCartClicked(
                    card,
                    add_to_cart_false,
                    add_to_cart
                )
            }))
        }
    }

    interface customOnClickListener {
        fun onFavouriteButtonClicked(card: Card?, button: ImageButton)
        fun onAddToShoppingCartClicked(card: Card?, button: Button, button2: Button)
        fun onCardViewClicked(card: Card?)
    }
}