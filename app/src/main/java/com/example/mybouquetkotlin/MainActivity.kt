package com.example.mybouquetkotlin

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mybouquetkotlin.data.Card
import com.example.mybouquetkotlin.data.Cards
import com.example.mybouquetkotlin.data.User
import com.example.mybouquetkotlin.fragments.AddFragment
import com.example.mybouquetkotlin.fragments.FavouriteFragment
import com.example.mybouquetkotlin.fragments.HomeFragment
import com.example.mybouquetkotlin.fragments.LoginFragment
import com.example.mybouquetkotlin.fragments.ShoppingCardFragment
import com.example.mybouquetkotlin.fragments.UserFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage

class MainActivity() : AppCompatActivity() {
    private var firestore: FirebaseFirestore? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var cards: Cards? = null
    private var homeFragment: HomeFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cards = Cards()

        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        cards!!.firestore = firestore
        cards!!.firebaseAuth = firebaseAuth


        //setFirestore();
        firestore!!.collection("bouquets").get()
            .addOnSuccessListener(object : OnSuccessListener<QuerySnapshot> {
                override fun onSuccess(queryDocumentSnapshots: QuerySnapshot) {
                    for (document: QueryDocumentSnapshot in queryDocumentSnapshots) {
                        val card: Card = document.toObject(
                            Card::class.java
                        )
                        card.path = document.getReference().getPath()
                        firestore!!.collection("bouquets").document(document.getId())
                            .update("path", document.getReference().getPath())
                        cards!!.cards.add(card)
                    }

                    if (firebaseAuth!!.getCurrentUser() != null) {
                        val UID: String = firebaseAuth!!.getCurrentUser()!!.getUid()
                        cards!!.UID = UID
                        Log.i("TAG", cards!!.UID!!)
                        firestore!!.collection("users").document(UID).get()
                            .addOnSuccessListener(object : OnSuccessListener<DocumentSnapshot> {
                                override fun onSuccess(documentSnapshot: DocumentSnapshot) {
                                    val user: User? = documentSnapshot.toObject(
                                        User::class.java
                                    )
                                    val favouriteCardsList: List<String?>? =
                                        user!!.favouriteCardsPath
                                    val toShoppingCardList: List<String?>? =
                                        user!!.cardsToShoppingCartPath
                                    cards!!.phoneNumber = user.phoneNumber
                                    for (cardPath: String? in favouriteCardsList!!) {
                                        for (card: Card? in cards!!.cards) {
                                            if ((card!!.path == cardPath)) {
                                                cards!!.favouriteCards.add(card)
                                                Log.i("TAG", "added to fav cards")
                                                break
                                            }
                                        }
                                    }

                                    for (cardPath: String? in toShoppingCardList!!) {
                                        for (card: Card? in cards!!.cards) {
                                            if ((card!!.path == cardPath)) {
                                                cards!!.toShoppingCartCards.add(card)
                                                Log.i("TAG", "added to shop cards")
                                                break
                                            }
                                        }
                                    }
                                    cards!!.ordersList = user.orders
                                    homeFragment = HomeFragment()
                                    val homeBundle: Bundle = Bundle()
                                    homeBundle.putSerializable("cards", cards)
                                    homeFragment!!.setArguments(homeBundle)
                                    Log.i("TAG", "UID: " + cards!!.UID)
                                    Log.i("TAG", "fav: " + cards!!.favouriteCards.size.toString())
                                    Log.i(
                                        "TAG",
                                        "shop: " + cards!!.toShoppingCartCards.size.toString()
                                    )
                                    getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment, homeFragment!!).commit()
                                }
                            })
                    } else {
                        homeFragment = HomeFragment()
                        val homeBundle: Bundle = Bundle()
                        homeBundle.putSerializable("cards", cards)
                        homeFragment!!.setArguments(homeBundle)
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, homeFragment!!).commit()
                    }

                    val bottomNavigationView: BottomNavigationView =
                        findViewById(R.id.bottom_navigation)
                    bottomNavigationView.setOnNavigationItemSelectedListener(object :
                        BottomNavigationView.OnNavigationItemSelectedListener {
                        override fun onNavigationItemSelected(item: MenuItem): Boolean {
                            if (item.getItemId() == R.id.home_screen) {
                                getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment, (homeFragment)!!).commit()
                                return true
                            } else if (item.getItemId() == R.id.favourite_screen) {
                                val favouriteFragment: FavouriteFragment = FavouriteFragment()
                                val favouriteBundle: Bundle = Bundle()
                                favouriteBundle.putSerializable("cards", cards)
                                Log.i("TAG", cards!!.favouriteCards.size.toString())
                                favouriteFragment.setArguments(favouriteBundle)
                                getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment, favouriteFragment).commit()
                                return true
                            } else if (item.getItemId() == R.id.custom_bouquet_screen) {
                                val addFragment: AddFragment = AddFragment()
                                val addBundle: Bundle = Bundle()
                                addBundle.putSerializable("cards", cards)
                                addFragment.setArguments(addBundle)
                                getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment, addFragment).commit()
                                return true
                            } else if (item.getItemId() == R.id.shopping_card_screen) {
                                val shoppingCardFragment: ShoppingCardFragment =
                                    ShoppingCardFragment()
                                val shoppingCartBundle: Bundle = Bundle()
                                shoppingCartBundle.putSerializable("cards", cards)
                                shoppingCardFragment.setArguments(shoppingCartBundle)
                                getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment, shoppingCardFragment).commit()
                                return true
                            } else if (item.getItemId() == R.id.profile_screen) {
                                Log.i("TAG", "UID " + cards!!.UID)
                                if (cards!!.UID == null) {
                                    val loginFragment: LoginFragment = LoginFragment()
                                    val loginBundle: Bundle = Bundle()
                                    loginBundle.putSerializable("cards", cards)
                                    loginFragment.setArguments(loginBundle)
                                    getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment, loginFragment).commit()
                                } else {
                                    val userFragment: UserFragment = UserFragment()
                                    val userBundle: Bundle = Bundle()
                                    userBundle.putSerializable("cards", cards)
                                    userFragment.setArguments(userBundle)
                                    getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment, userFragment).commit()
                                }
                                return true
                            }
                            return false
                        }
                    })
                }
            })
    }

   /* fun setFirestore() {
        val bouquets: MutableList<Card> = ArrayList()
        bouquets.add(
            Card(
                "Букет живых цветов из орхидей с красными розами",
                4990,
                "https://www.prostocvet.ru/upload/resize_cache/iblock/019/600_600_140cd750bba9870f18aada2478b24840a/i647elgnhpx3hxip9srsyum65w96917v.webp",
                "Флористический букет из орхидей космического сине-голубого цвета и ярко-красной розы. Букет, повторяющий цвет волос главной героини потрясающего фильма о любви. И, конечно же, нам тоже хотелось бы уметь стирать память, но только для того, чтобы вы могли снова и снова влюбляться в этот необыкновенный букет.\\n"
            )
        )
        bouquets.add(
            Card(
                "Букет Бизнеследи",
                6490,
                "https://www.prostocvet.ru/upload/resize_cache/iblock/4a9/600_600_140cd750bba9870f18aada2478b24840a/57020et93gwd0wgp5kmhv6jsfn04at4a.webp",
                ("Рецепт нашего флористического букета «Бизнеследи L»:\n" +
                        "- центром нашей композиции представляем премиальную розу «Мондиал»  \n" +
                        "- задаём мягкую розовую тональность пышной кустовой розой\n" +
                        "- разбавляем миниатюрной хризантемой\n" +
                        "- придаём лёгкость изящной альстромерией и белоснежной статицей\n" +
                        "- приправляем ветками эвкалипта и листьями салал\n" +
                        "- заворачиваем в бумагу в тон букета и завязываем шелковой лентой.\n" +
                        "- ставим в воду с кризалом\n" +
                        "- отправляем в холодильную камеру")
            )
        )
        bouquets.add(
            Card(
                "Букет Обаятельный босс",
                8190,
                "https://www.prostocvet.ru/upload/resize_cache/iblock/54c/600_600_140cd750bba9870f18aada2478b24840a/moy7vx7ja2rofaqf15o2ulh9epsu4hwa.webp",
                ("Рецепт нашего флористического букета «Обаятельный босс»:\n" +
                        "- берем в качестве основы премиальные розы сорта «Эвер Ред»\n" +
                        "- добавляем ароматную гвоздику контрастного колора\n" +
                        "- сдабриваем облачной гортензией (гидрандия)\n" +
                        "- декорируем алыми ягодами гиперикума\n" +
                        "- зеленью салал, ветками эвкалипта и грин Бел придаём пышность и воздушность\n" +
                        "- заворачиваем в бумагу нежного оттенка и завязываем шелковой лентой.\n" +
                        "- ставим в воду с кризалом\n" +
                        "- отправляем в холодильную камеру")
            )
        )
        bouquets.add(
            Card(
                "Букет Аврора",
                3990,
                "https://www.prostocvet.ru/upload/resize_cache/iblock/e47/600_600_140cd750bba9870f18aada2478b24840a/6ru40jjhs378d0mwgjbh5bivj8fql3uy.webp",
                "Букет, разработанный командой профессиональных флористов.\n" +
                        "Идеально подойдет под любой формат торжества и по любому поводу, будь то романтическая прогулка, юбилей, профессиональный праздник любимого учителя или день рождения близкого человека."
            )
        )
        bouquets.add(
            Card(
                "Букет Романс",
                4590,
                "https://www.prostocvet.ru/upload/resize_cache/iblock/a0b/600_600_140cd750bba9870f18aada2478b24840a/ea0pkzpnlndz7c196f3sgj25x1b3cywo.webp",
                "Букет, разработанный командой профессиональных флористов.\n" +
                        "Идеально подойдет под любой формат торжества и по любому поводу, будь то романтическая прогулка, юбилей, профессиональный праздник любимого учителя или день рождения близкого человека."
            )
        )
        bouquets.add(
            Card(
                "Букет цветов из розовой гортензии и гвоздик\"Нежность\"",
                2890,
                "https://www.prostocvet.ru/upload/iblock/860/bln3hxc2om8tcme6cfj02m3h1y2jxnyn.webp",
                "Букет, напоминающий зефирку или сладкую вату на палочке. В составе нежно-розовая гидрангия (гортензия), утонченный розовый диантус (гвоздика), розовая статица и пушистая розовая стифа."
            )
        )

        for (card: Card? in bouquets) {
            firestore!!.collection("bouquets").add((card)!!)
        }
    }*/
}