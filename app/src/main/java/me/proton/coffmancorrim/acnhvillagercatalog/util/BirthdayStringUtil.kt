package me.proton.coffmancorrim.acnhvillagercatalog.util

import me.proton.coffmancorrim.acnhvillagercatalog.model.Birthday

class BirthdayStringUtil {
    companion object {
        fun formatBirthdayString(birthdayMonth: String, birthday: String): String{
            if (birthday == "1"){
                return "Birthday: ${birthdayMonth} ${birthday}st"
            }else if(birthday == "2"){
                return "Birthday: ${birthdayMonth} ${birthday}nd"
            }else if(birthday == "3"){
                return "Birthday: ${birthdayMonth} ${birthday}rd"
            } else {
                return "Birthday: ${birthdayMonth} ${birthday}th"
            }
        }
    }
}