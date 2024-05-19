package com.example.mybouquetkotlin.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.data.Cards

class AddFragment : Fragment() {
    private var cards: Cards? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            cards = arguments!!["cards"] as Cards?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add, container, false)

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
        }
        return rootView
    }
}