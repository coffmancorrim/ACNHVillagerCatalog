package me.proton.coffmancorrim.acnhvillagercatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val villagersListFragment = VillagersListFragment()
        fragmentTransaction.replace(R.id.fragment_container_view, villagersListFragment)
        fragmentTransaction.commit()
    }
}