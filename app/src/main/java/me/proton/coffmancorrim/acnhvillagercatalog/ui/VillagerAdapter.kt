package me.proton.coffmancorrim.acnhvillagercatalog.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
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
import me.proton.coffmancorrim.acnhvillagercatalog.databinding.ItemVillagerBinding
import me.proton.coffmancorrim.acnhvillagercatalog.ui.common.VillagerDetailFragment
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.util.BirthdayStringUtil
import me.proton.coffmancorrim.acnhvillagercatalog.util.FragmentUtil
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VillagerViewHolder {
        val binding = ItemVillagerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return VillagerViewHolder(binding)
    }

    inner class VillagerViewHolder(private val binding: ItemVillagerBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageVillagerIcon: ImageView = binding.imageVillagerIcon
        val textVillagerName: TextView = binding.textVillagerName
        val textVillagerBirthDate: TextView = binding.textVillagerBirthdate
        val optionsIcon: ImageView = binding.iconOptions
        val favoritesIcon: ImageView = binding.iconFavorite
        val favoritesIconFilled: ImageView = binding.iconFavoriteFilled
        val disableView: View = binding.viewDisableLayout
    }

    override fun onBindViewHolder(villagerViewHolder: VillagerViewHolder, position: Int) {
        val villager = filteredList[position]

        val backgroundCircle = ShapeDrawable(OvalShape())
        backgroundCircle.paint.color = Color.parseColor("#${villager.textColor}")
        villagerViewHolder.imageVillagerIcon.background = backgroundCircle
        Glide
            .with(villagerViewHolder.itemView.context)
            .load(villager.nhDetails.iconUrl)
            .placeholder(R.drawable.placeholder)
            .into(villagerViewHolder.imageVillagerIcon)

        villagerViewHolder.textVillagerName.text = villager.name
        villagerViewHolder.textVillagerBirthDate.text = BirthdayStringUtil.formatBirthdayString(villager.birthdayMonth, villager.birthdayDay)
        val textColor = Color.parseColor("#${villager.textColor}")
        villagerViewHolder.textVillagerName.setTextColor(textColor)
        villagerViewHolder.textVillagerBirthDate.setTextColor(textColor)
        villagerViewHolder.itemView.setBackgroundColor(Color.parseColor("#${villager.titleColor}"))

        if (mainViewModel.isVillagerInList(villager, mainViewModel.favoritesList.value)){
            villagerViewHolder.favoritesIcon.visibility = View.GONE
            villagerViewHolder.favoritesIconFilled.visibility = View.VISIBLE
        }



        villagerViewHolder.favoritesIcon.setOnClickListener {
            if (villagerViewHolder.favoritesIcon.visibility == View.VISIBLE){
                villagerViewHolder.favoritesIcon.visibility = View.GONE
                villagerViewHolder.favoritesIconFilled.visibility = View.VISIBLE

                mainViewModel.addFavoriteVillager(villager)
                mainViewModel.setFavorite(villager.id, !villager.favorite)
            }
        }

        villagerViewHolder.favoritesIconFilled.setOnClickListener {
            if (villagerViewHolder.favoritesIconFilled.visibility == View.VISIBLE){
                villagerViewHolder.favoritesIconFilled.visibility = View.GONE
                villagerViewHolder.favoritesIcon.visibility = View.VISIBLE

                mainViewModel.setFavorite(villager.id, !villager.favorite)
                mainViewModel.removeFavoriteVillager(villager)

                if (mainViewModel.isFavoritesList.value){
                    notifyDataSetChanged()
                }
            }
        }

        villagerViewHolder.optionsIcon.setOnClickListener {
            showOptionsMenu(villagerViewHolder.optionsIcon, position, villagerViewHolder.optionsIcon.context)
        }

        if(!mainViewModel.isListClickable.value){
            villagerViewHolder.disableView.visibility = View.VISIBLE
            villagerViewHolder.disableView.setOnClickListener {
                bottomNavigationView.selectedItemId = parentId
                Log.d("RELOAD", "ON CLICK VILLAGER VIEW HOLDER")
                if (mainViewModel.reloadVillagerData && parentId == R.id.item_discover){
                    mainViewModel.toggleReloadVillagerData()
                }
            }
        }else{
            villagerViewHolder.disableView.visibility = View.GONE
        }

        villagerViewHolder.itemView.setOnClickListener {
            mainViewModel.detailVillager = filteredList[position]
            FragmentUtil.replaceFragment(fragmentManager, VillagerDetailFragment(), R.id.fragment_container_view, true)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    private fun showOptionsMenu(imageView: ImageView, position: Int, context: Context) {
        val optionsMenu = PopupMenu(context, imageView)
        optionsMenu.inflate(R.menu.menu_villager_options)

        optionsMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add_to_list -> {
                    showListDialogPopup(mainViewModel.listOfNames.value, position, item.itemId, context)
                    true
                }
                R.id.action_delete_from_list -> {
                    showListDialogPopup(mainViewModel.listOfNames.value, position, item.itemId, context)
                    true
                }
                else -> false
            }
        }
        optionsMenu.show()
    }

    private fun showListDialogPopup(list: List<ListWrapper>, position: Int, itemId: Int, context: Context) {
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

    fun filter(query: String) { //TODO remove log statements here, make sure code is not redundant (clean code).
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