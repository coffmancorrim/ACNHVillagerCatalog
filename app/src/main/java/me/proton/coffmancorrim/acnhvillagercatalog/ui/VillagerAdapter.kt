package me.proton.coffmancorrim.acnhvillagercatalog.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.model.Birthday
import me.proton.coffmancorrim.acnhvillagercatalog.model.Gender
import me.proton.coffmancorrim.acnhvillagercatalog.model.Hobby
import me.proton.coffmancorrim.acnhvillagercatalog.model.NhDetails
import me.proton.coffmancorrim.acnhvillagercatalog.model.Personality
import me.proton.coffmancorrim.acnhvillagercatalog.model.Species
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import kotlin.random.Random

class VillagerAdapter(val villagerList: List<Villager> = generateVillagerList(5)) : RecyclerView.Adapter<VillagerAdapter.VillagerViewHolder>() {

    inner class VillagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageVillagerIcon = itemView.findViewById<ImageView>(R.id.image_villager_icon)
        val textVillagerName = itemView.findViewById<TextView>(R.id.text_villager_name)
        val textVillagerCatchPhrase = itemView.findViewById<TextView>(R.id.text_villager_catchphrase)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VillagerViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_villager, viewGroup, false)
        return VillagerViewHolder(itemView)
    }

    override fun onBindViewHolder(villagerViewHolder: VillagerViewHolder, position: Int) {
        val villager = villagerList[position]
        villagerViewHolder.itemView.setBackgroundColor(Color.parseColor(villager.titleColor))
        villagerViewHolder.imageVillagerIcon.setImageResource(R.drawable.place_holder_villager_icon)
        villagerViewHolder.textVillagerName.text = villager.name
        villagerViewHolder.textVillagerCatchPhrase.text = villager.nhDetails.catchphrase
    }

    override fun getItemCount(): Int {
        return villagerList.size
    }


    companion object{

        fun generateVillagerList(numberOfVillagers: Int) : List<Villager>{
            val dummyList = mutableListOf<Villager>()
            for (i in 0 until  numberOfVillagers){
                dummyList.add(generateDummyVillager())
            }
            return dummyList
        }

        fun generateDummyVillager(): Villager {
            val names = listOf("Bob", "Alice", "Tom", "Sally", "Charlie")
            val personalities = listOf("Big sister", "Cranky", "Jock", "Lazy", "Normal", "Peppy", "Smug", "Snooty")
            val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
            val days = (1..31).map { it.toString() }
            val signs = listOf("Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces")
            val colors = listOf("Red", "Blue", "Green", "Yellow", "Purple", "Orange", "Pink", "Brown", "Black", "White")

            val random = Random.Default

            return Villager(
                name = names.random(),
                gender = Gender.values().random(),
                personality = Personality.values().random(),
                species = Species.values().random(),
                birthday = Birthday(months.random(), days.random()),
                titleColor = generateRandomColor(),
                textColor = generateRandomColor(),
                id = "cat${random.nextInt(100)}",
                nhDetails = NhDetails(
                    iconUrl = "https://example.com/icon/${random.nextInt(100)}",
                    quote = "Random quote ${random.nextInt(100)}",
                    catchphrase = "Random catchphrase ${random.nextInt(100)}",
                    favStyles = listOf("Style1", "Style2"),
                    favColors = colors.shuffled().take(2),
                    hobby = Hobby.values().random(),
                    houseExteriorUrl = "https://example.com/house/${random.nextInt(100)}"
                )
            )
        }

        private fun generateRandomColor(): String {
            val random = Random.Default
            val color = random.nextInt(0xFFFFFF + 1)
            return String.format("#%06X", color)
        }

    }
}