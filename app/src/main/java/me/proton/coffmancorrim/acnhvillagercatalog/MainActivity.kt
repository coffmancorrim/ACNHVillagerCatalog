package me.proton.coffmancorrim.acnhvillagercatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.data.VillagerDatabaseSingleton
import me.proton.coffmancorrim.acnhvillagercatalog.data.ListWrapperDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.ui.fragments.CustomListsFragment
import me.proton.coffmancorrim.acnhvillagercatalog.ui.fragments.DiscoverFragment
import me.proton.coffmancorrim.acnhvillagercatalog.ui.fragments.FavoriteFragment
import me.proton.coffmancorrim.acnhvillagercatalog.ui.fragments.HomeFragment
import me.proton.coffmancorrim.acnhvillagercatalog.util.FragmentUtil
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private  lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewModelFactory = MainViewModelFactory(AnimalCrossingApplication.appModule.animalCrossingRepository)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.item_home -> {
                    FragmentUtil.replaceFragment(supportFragmentManager, HomeFragment(), R.id.fragment_container_view)
                }
                R.id.item_discover -> {
                    FragmentUtil.replaceFragment(supportFragmentManager, DiscoverFragment(), R.id.fragment_container_view)
                }
                R.id.item_favorites -> {
                    FragmentUtil.replaceFragment(supportFragmentManager, FavoriteFragment(), R.id.fragment_container_view)
                }
                R.id.item_custom -> {
                    FragmentUtil.replaceFragment(supportFragmentManager, CustomListsFragment(), R.id.fragment_container_view)
                }
            }
            true
        }

        FragmentUtil.replaceFragment(supportFragmentManager, HomeFragment(), R.id.fragment_container_view)
    }

    override fun onStop() {
        super.onStop()

        lifecycleScope.launch(){
            mainViewModel.updateDao()
        }
    }

}