package com.example.mybouquetkotlin.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mybouquetkotlin.ViewModel.Fragments.AddViewModel
import com.example.mybouquetkotlin.databinding.FragmentAddBinding

class AddFragment : Fragment() {
    private lateinit var viewModel:AddViewModel
    private lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AddViewModel::class.java]

        binding.addCustomOrder.setOnClickListener {
            //verify data
            viewModel.makeOrder(binding.bouquetDescriptionCustomBouquet.text.toString())
            binding.bouquetDescriptionCustomBouquet.text = null
            Toast.makeText(context, "thx", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
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