package me.proton.coffmancorrim.acnhvillagercatalog.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.ui.common.VillagerDetailFragment
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel

class VillagerAdapter(
    private val villagerList: List<Villager>,
    private val fragmentManager: FragmentManager,
    private val mainViewModel: MainViewModel,
    private val parentId: Int,
    private val bottomNavigationView: BottomNavigationView
    )
    : RecyclerView.Adapter<VillagerAdapter.VillagerViewHolder>() {

    private var filteredList: List<Villager> = villagerList

    inner class VillagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageVillagerIcon = itemView.findViewById<ImageView>(R.id.image_villager_icon)
        val textVillagerName = itemView.findViewById<TextView>(R.id.text_villager_name)
        val textVillagerCatchPhrase = itemView.findViewById<TextView>(R.id.text_villager_catchphrase)
        val optionsIcon = itemView.findViewById<ImageView>(R.id.icon_options)
        val favoritesIcon = itemView.findViewById<ImageView>(R.id.icon_favorite)
        val favoritesIconFilled = itemView.findViewById<ImageView>(R.id.icon_favorite_filled)

        val disableView = itemView.findViewById<View>(R.id.viewDisableLayout)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VillagerViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_villager, viewGroup, false)
        return VillagerViewHolder(itemView)
    }

    override fun onBindViewHolder(villagerViewHolder: VillagerViewHolder, position: Int) {
        val villager = filteredList[position]

        val colorString = "#${villager.titleColor}"
        villagerViewHolder.itemView.setBackgroundColor(Color.parseColor(colorString))

        Glide
            .with(villagerViewHolder.itemView.context)
            .load("ignorethis")
            .placeholder(R.drawable.placeholder)
            .into(villagerViewHolder.imageVillagerIcon)

        villagerViewHolder.textVillagerName.text = villager.name
        villagerViewHolder.textVillagerCatchPhrase.text = villager.nhDetails.catchphrase

        if (mainViewModel.isVillagerInList(villager, mainViewModel.favoritesList.value)){
            villagerViewHolder.favoritesIcon.visibility = View.GONE
            villagerViewHolder.favoritesIconFilled.visibility = View.VISIBLE
        }

        villagerViewHolder.optionsIcon.setOnClickListener {
            showPopupMenu(villagerViewHolder.optionsIcon, position, villagerViewHolder.optionsIcon.context)
        }

        villagerViewHolder.favoritesIcon.setOnClickListener {
            if (villagerViewHolder.favoritesIcon.visibility == View.VISIBLE){
                villagerViewHolder.favoritesIcon.visibility = View.GONE
                villagerViewHolder.favoritesIconFilled.visibility = View.VISIBLE

                villager.favorite = true
                mainViewModel.addFavoriteVillager(villager)
            }
        }

        villagerViewHolder.favoritesIconFilled.setOnClickListener {
            if (villagerViewHolder.favoritesIconFilled.visibility == View.VISIBLE){
                villagerViewHolder.favoritesIconFilled.visibility = View.GONE
                villagerViewHolder.favoritesIcon.visibility = View.VISIBLE

                villager.favorite = false
                mainViewModel.removeFavoriteVillager(villager)

                if (mainViewModel.isFavoritesList.value){
                    notifyDataSetChanged()
                }
            }
        }

        Log.d("IS_CLICKABLE", mainViewModel.isListClickable.value.toString())
        if(!mainViewModel.isListClickable.value){
            villagerViewHolder.disableView.visibility = View.VISIBLE
            villagerViewHolder.disableView.setOnClickListener {
                Log.d("IS_CLICKABLE", "mainViewModel.isListClickable.value.toString()")
                bottomNavigationView.selectedItemId = parentId

                Log.d("RELOAD", "ON CLICK VILLAGER VIEW HOLDER")
                if (mainViewModel.reloadVillagerData && parentId == R.id.item_discover){
                    mainViewModel.toggleReloadVillagerData()
                }
            }
        }else{
            villagerViewHolder.disableView.visibility = View.GONE
            villagerViewHolder.disableView.setOnClickListener {

            }
        }

        villagerViewHolder.itemView.setOnClickListener {
            mainViewModel.detailVillager = filteredList[position]
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container_view, VillagerDetailFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    private fun showPopupMenu(imageView: ImageView, position: Int, context: Context) {
        val popupMenu = PopupMenu(context, imageView)
        popupMenu.inflate(R.menu.menu_villager_options)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add_to_list -> {

                    showOptionsDialog(mainViewModel.listOfNames.value, position, item.itemId, context)
                    true
                }
                R.id.action_delete_from_list -> {

                    showOptionsDialog(mainViewModel.listOfNames.value, position, item.itemId, context)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    fun showOptionsDialog(list: List<ListWrapper>, position: Int, itemId: Int, context: Context) {
        val options = list.map { it.listName }.toTypedArray()

        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Select a list")

        builder.setItems(options) { _, optionIndex ->
            val selectedList = list[optionIndex]
            if(itemId == R.id.action_add_to_list){
                mainViewModel.addVillagerToCustomList(selectedList.keyId, filteredList[position])
            } else if (itemId == R.id.action_delete_from_list){
                mainViewModel.removeVillagerFromCustomList(selectedList.keyId, filteredList[position])
            }


        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    fun filter(query: String) {
        Log.d("Filter", "Filtering with query: $query")

        filteredList = if (query.isEmpty()) {
            Log.d("Filter", "Query is empty, resetting to original list")
            villagerList.toList()
        } else {
            val filtered = villagerList.filter { item ->
                val nameContains = item.name.contains(query, ignoreCase = true)
                val speciesContains = item.species.name.contains(query, ignoreCase = true)
                val personalityContains = item.personality.name.contains(query, ignoreCase = true)
                val birthMonthContains = item.birthdayMonth.contains(query, ignoreCase = true)
                val contains = nameContains || speciesContains || personalityContains || birthMonthContains

                Log.d("Filter", "Item: ${item.name}, Query: $query, Name Contains: $nameContains, Species Contains: $speciesContains, Personality Contains: $personalityContains, BirthMonth Contains: $birthMonthContains, Contains: $contains")
                contains
            }
            Log.d("Filter", "Filtered list size: ${filtered.size}")
            filtered
        }

        notifyDataSetChanged()
    }




}