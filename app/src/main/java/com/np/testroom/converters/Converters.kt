
package com.np.testroom.converters

import androidx.room.TypeConverter
import com.np.testroom.enums.ScenarioCategory

class Converters {

    @TypeConverter
    fun fromCategory(category: ScenarioCategory?): String? {
        return category?.name
    }

    @TypeConverter
    fun toCategory(category: String?): ScenarioCategory? {
        return if (category != null) {
            ScenarioCategory.valueOf(category)
        } else {
            null
        }
    }
}
