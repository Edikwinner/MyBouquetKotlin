package com.example.mybouquetkotlin.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.data.Card
import com.example.mybouquetkotlin.data.Cards
import com.example.mybouquetkotlin.recycler_view_shopping_cart_screen.ShoppingCartScreenAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class ShoppingCardFragment() : Fragment() {
    var totalCost: Int = 0
    private lateinit var totalCostTextView: TextView
    private var cards: Cards? = null
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var shoppingCartScreenAdapter: ShoppingCartScreenAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            cards = requireArguments().get("cards") as Cards?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_shopping_card, container, false)
        totalCost = 0
        if (cards!!.UID == null) {
            val toast: Toast = Toast.makeText(
                getContext(),
                "Для просмотра корзины необходимо зарегистрироваться",
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        totalCostTextView = rootView.findViewById(R.id.total_cost_in_shopping_cart)

        firebaseAuth = cards!!.firebaseAuth


        recyclerView = rootView.findViewById(R.id.shopping_card_recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(getContext()))

        for (card: Card? in cards!!.toShoppingCartCards) {
            totalCost += card!!.bouquetCost
        }
        totalCostTextView.setText(totalCost.toString() + " ₽")
        shoppingCartScreenAdapter = ShoppingCartScreenAdapter(cards!!.toShoppingCartCards)
        shoppingCartScreenAdapter!!.setListener(object :
            ShoppingCartScreenAdapter.customOnClickListener {
            override fun deleteCard(card: Card?) {
                deleteCard(card)
            }
        })
        recyclerView.setAdapter(shoppingCartScreenAdapter)
        val cardsTouchHelper: ItemTouchHelper = getItemTouchHelper(shoppingCartScreenAdapter!!)
        cardsTouchHelper.attachToRecyclerView(recyclerView)

        rootView.findViewById<View>(R.id.delete_all_shopping_cart)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    val builder: MaterialAlertDialogBuilder =
                        MaterialAlertDialogBuilder(requireContext())
                    builder.setTitle("Подтверждение")
                        .setMessage("Вы уверены что хотите очистить корзину?")
                        .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                        .setPositiveButton("Да", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                deleteAll()
                            }
                        }).setNegativeButton("Отмена", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                dialog.dismiss()
                            }
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            })

        rootView.findViewById<View>(R.id.make_order)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (cards!!.phoneNumber === "" || cards!!.phoneNumber == null) {
                        Toast.makeText(
                            getContext(),
                            "Заполните Ваш номер телефона",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val builder: MaterialAlertDialogBuilder =
                            MaterialAlertDialogBuilder(requireContext())
                        builder.setTitle("Спасибо за заказ")
                        builder.setMessage("В ближайшее время Вам перезвонит наш менеджер")
                        builder.setNeutralButton("OK", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                dialog.dismiss()
                            }
                        })
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                        for (card: Card? in cards!!.toShoppingCartCards) {
                            cards!!.addOrder(card!!.path)
                        }
                        deleteAll()
                    }
                }
            })
        return rootView
    }

    private fun deleteCard(card: Card?) {
        Log.i("TAG", "deleteCard: ")
        cards!!.deleteCardFromShoppingCards(card)
        shoppingCartScreenAdapter!!.notifyDataSetChanged()
        totalCost -= card!!.bouquetCost
        totalCostTextView!!.setText(totalCost.toString() + " ₽")
    }

    private fun deleteAll() {
        Log.i("TAG", (cards!!.UID)!!)
        cards!!.deleteAllFromShoppingCards()
        shoppingCartScreenAdapter!!.notifyDataSetChanged()
        totalCostTextView!!.setText("0 ₽")
    }

    private fun getItemTouchHelper(adapter: ShoppingCartScreenAdapter): ItemTouchHelper {
        val simpleItemTouchCallback
                : ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position: Int = viewHolder.getAdapterPosition()
                val card: Card? = cards!!.toShoppingCartCards.get(position)
                deleteCard(card)
            }
        }

        return ItemTouchHelper(simpleItemTouchCallback)
    }
}