package me.proton.coffmancorrim.acnhvillagercatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val homeFragment = HomeFragment()
        fragmentTransaction.replace(R.id.fragment_container_view, homeFragment)
        fragmentTransaction.commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener{item ->

            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)


            val fragmentTransaction = supportFragmentManager.beginTransaction()
            when (item.itemId){
                R.id.item_home -> {
                    fragmentTransaction.replace(R.id.fragment_container_view, HomeFragment())
                }
                R.id.item_discover -> {
                    fragmentTransaction.replace(R.id.fragment_container_view, DiscoverFragment())
                }
                R.id.item_favorites -> {
                    fragmentTransaction.replace(R.id.fragment_container_view, FavoriteFragment())
                }
                R.id.item_custom -> {
                    fragmentTransaction.replace(R.id.fragment_container_view, CustomListsFragment())
                }
            }
            fragmentTransaction.commit()
            true
        }
    }




}