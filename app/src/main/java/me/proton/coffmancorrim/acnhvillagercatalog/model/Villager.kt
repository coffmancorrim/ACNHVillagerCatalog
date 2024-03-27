package me.proton.coffmancorrim.acnhvillagercatalog.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "villagers")
data class Villager(
    @PrimaryKey
    @SerialName("name")
    val name: String,
    @SerialName("gender")
    val gender: Gender,
    @SerialName("personality")
    val personality: Personality,
    @SerialName("species")
    val species: Species,

    @SerialName("birthday_month")
    val birthdayMonth: String,
    @SerialName("birthday_day")
    val birthdayDay: String,

    @SerialName("title_color")
    val titleColor: String,
    @SerialName("text_color")
    val textColor: String,
    @SerialName("id")
    val id: String,
    @SerialName("nh_details")
    val nhDetails: NhDetails,

    var favorite: Boolean = false,
    var listWrapperIds: List<String>? = null
)

@Serializable
data class Birthday(
    @SerialName("month")
    val month: String,
    @SerialName("day")
    val day: String
)

@Serializable
data class NhDetails(
    @SerialName("icon_url")
    val iconUrl: String,
    @SerialName("quote")
    val quote: String,
    @SerialName("catchphrase")
    val catchphrase: String,
    @SerialName("fav_styles")
    val favStyles: List<String>,
    @SerialName("fav_colors")
    val favColors: List<String>,
    @SerialName("hobby")
    val hobby: Hobby,
    @SerialName("house_exterior_url")
    val houseExteriorUrl: String
)

@Serializable
enum class Gender {
    Male,
    Female
}

@Serializable
enum class Personality {
    BigSister,
    Cranky,
    Jock,
    Sisterly,
    Lazy,
    Normal,
    Peppy,
    Smug,
    Snooty
}

@Serializable
enum class Species {
    Alligator,
    Anteater,
    Bear,
    BearCub,
    Bird,
    Bull,
    Cat,
    Cub,
    Chicken,
    Cow,
    Deer,
    Dog,
    Duck,
    Eagle,
    Elephant,
    Frog,
    Goat,
    Gorilla,
    Hamster,
    Hippo,
    Horse,
    Koala,
    Kangaroo,
    Lion,
    Monkey,
    Mouse,
    Octopus,
    Ostrich,
    Penguin,
    Pig,
    Rabbit,
    Rhino,
    Rhinoceros,
    Sheep,
    Squirrel,
    Tiger,
    Wolf
}

@Serializable
enum class Hobby {
    Education,
    Fashion,
    Fitness,
    Music,
    Nature,
    Play
}
