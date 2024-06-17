package com.example.mybouquetkotlin.View.Fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.Model.Cards
import com.example.mybouquetkotlin.ViewModel.Fragments.AddViewModel

class AddFragment : Fragment() {
    private lateinit var viewModel:AddViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add, container, false)
        viewModel = ViewModelProvider(this)[AddViewModel::class.java]
        val descriptionEditText = rootView.findViewById<EditText>(R.id.bouquet_description_custom_bouquet)

        val makeOrder = rootView.findViewById<Button>(R.id.add_custom_order)

        makeOrder.setOnClickListener {
            //verify data
            viewModel.makeOrder(descriptionEditText.text.toString())
            descriptionEditText.text = null
            Toast.makeText(context, "thx", Toast.LENGTH_SHORT).show()
        }
/*
        val bouquetDescription =
            rootView.findViewById<EditText>(R.id.bouquet_description_custom_bouquet)
        rootView.findViewById<View>(R.id.add_custom_order).setOnClickListener {
            if (cards!!.phoneNumber === "" || cards!!.phoneNumber == null) {
                Toast.makeText(context, "Заполните Ваш номер телефона", Toast.LENGTH_SHORT).show()
            } else {
                val toast = Toast.makeText(
                    context, """
     Спасибо за заказ
     В ближайшее время мы Вам перезвоним
     """.trimIndent(), Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                cards!!.addOrder(bouquetDescription.text.toString())
                bouquetDescription.setText("")
            }
        }
        if (cards!!.UID == null) {
            val toast = Toast.makeText(
                context,
                "Для заказа собственного букета необходимо зарегистрироваться",
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }*/
        return rootView
    }
}