package me.proton.coffmancorrim.acnhvillagercatalog.model

data class Villager(
    val name: String,
    val gender: Gender,
    val personality: Personality,
    val species: Species,
    val birthday: Birthday,
    val titleColor: String,
    val textColor: String,
    val id: String,
    val nhDetails: NhDetails
)

data class Birthday(
    val month: String,
    val day: String
)

data class NhDetails(
    val iconUrl: String,
    val quote: String,
    val catchphrase: String,
    val favStyles: List<String>,
    val favColors: List<String>,
    val hobby: Hobby,
    val houseExteriorUrl: String
)

enum class Gender {
    Male,
    Female
}

enum class Personality {
    BigSister,
    Cranky,
    Jock,
    Lazy,
    Normal,
    Peppy,
    Smug,
    Snooty
}

enum class Species {
    Alligator,
    Anteater,
    Bear,
    BearCub,
    Bird,
    Bull,
    Cat,
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
    Rhinoceros,
    Sheep,
    Squirrel,
    Tiger,
    Wolf
}

enum class Hobby {
    Education,
    Fashion,
    Fitness,
    Music,
    Nature,
    Play
}
