package me.proton.coffmancorrim.acnhvillagercatalog.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentUtil {

    companion object{
        fun replaceFragment(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            containerId: Int,
            addToBackStack: Boolean = false
        ) {
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(containerId, fragment)
            if(addToBackStack){
                fragmentTransaction.addToBackStack(null)
            }
            fragmentTransaction.commit()
        }
    }

}
