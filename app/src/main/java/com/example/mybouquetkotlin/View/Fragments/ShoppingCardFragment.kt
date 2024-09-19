package com.example.mybouquetkotlin.View.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybouquetkotlin.Model.Adapters.ShoppingCartScreenAdapter
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.ViewModel.Fragments.ShoppingCardViewModel
import com.example.mybouquetkotlin.databinding.FragmentShoppingCardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoppingCardFragment : Fragment(), ShoppingCartScreenAdapter.customOnClickListener {
    private val viewModel by viewModel<ShoppingCardViewModel>()
    private lateinit var adapter: ShoppingCartScreenAdapter
    private lateinit var binding: FragmentShoppingCardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingCardBinding.inflate(inflater)

        viewModel.refreshData()

        viewModel.totalSum.observe(viewLifecycleOwner, Observer {
            binding.totalCostInShoppingCart.text = "$it ₽"
        })
        binding.shoppingCardRecyclerView.setLayoutManager(LinearLayoutManager(context))
        adapter = ShoppingCartScreenAdapter(viewModel.shoppingCards.value ?: ArrayList<Card>(), this)
        getItemTouchHelper().attachToRecyclerView(binding.shoppingCardRecyclerView)
        binding.shoppingCardRecyclerView.adapter = adapter
        viewModel.shoppingCards.observe(viewLifecycleOwner, Observer {
            var sum = 0
            for (card in it){
                sum += card.bouquetCost
            }
            viewModel.totalSum.value = sum
            adapter = ShoppingCartScreenAdapter(it, this)
            binding.shoppingCardRecyclerView.adapter = adapter
        })
        val cardsTouchHelper: ItemTouchHelper? = null
        cardsTouchHelper?.attachToRecyclerView(binding.shoppingCardRecyclerView)

        binding.makeOrder.setOnClickListener {
            val paths = ArrayList<String>()
            viewModel.shoppingCards.value!!.forEach {
                paths.add(it.path)
            }
            viewModel.makeOrder(paths)
            viewModel.deleteCards()
            Toast.makeText(context, "thx", Toast.LENGTH_SHORT).show()
        }

        binding.deleteAllShoppingCart.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            builder.setTitle("Подтверждение")
                .setMessage("Вы уверены что хотите очистить корзину?")
                .setIcon(resources.getDrawable(android.R.drawable.ic_dialog_alert))
                .setPositiveButton("Да") { dialog, which ->
                    viewModel.deleteCards()
                }.setNegativeButton("Отмена") { dialog, which ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return binding.root
    }

    override fun deleteCard(card: Card) {
        Log.i("TAG", "deleteCard: ")
        adapter.notifyDataSetChanged()
        viewModel.totalSum.value = viewModel.totalSum.value!! - card.bouquetCost
        viewModel.deleteCard(card)
    }

    override fun onCardViewClicked(card: Card) {
        val bundle = Bundle()
        bundle.putSerializable("card", card)
        bundle.putBoolean("favourite", viewModel.favouriteCards.value!!.contains(card))
        bundle.putBoolean("shopping", viewModel.shoppingCards.value!!.contains(card))
        binding.root.findNavController().navigate(R.id.action_shoppingCardFragment_to_descriptionFragment, bundle)
        Log.i("TAG", "onCardViewClicked: ")
    }

    private fun getItemTouchHelper(): ItemTouchHelper {
        val simpleItemTouchCallback = object : SimpleCallback(
            0,
            LEFT or RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                swipeDir: Int
            ) {
                val position: Int = viewHolder.getAdapterPosition()
                val card: Card = viewModel.shoppingCards.value!![position]
                deleteCard(card)
            }
        }
        return ItemTouchHelper(simpleItemTouchCallback)
    }
}
