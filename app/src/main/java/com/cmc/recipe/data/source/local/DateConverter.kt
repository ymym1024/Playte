package com.cmc.recipe.data.source.local

import androidx.room.TypeConverter
import com.cmc.recipe.utils.DateUtil
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: String?): Date? {
        return timestamp?.let { DateUtil.dbDateFormat.parse(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): String? {
        return DateUtil.dbDateFormat.format(date)
    }
}