package com.np.testroom.utils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.text.SimpleDateFormat
import java.util.*
import org.mindrot.jbcrypt.BCrypt
class commonFuncs {
    companion object {
        fun convertMonthsToYearsMonths(months: Int): String {
            val years = months / 12
            val remainingMonths = months % 12

            return when {
                years > 0 && remainingMonths > 0 -> {
                    "$years year${if (years > 1) "s" else ""} $remainingMonths month${if (remainingMonths > 1) "s" else ""}"
                }
                years > 0 -> {
                    "$years year${if (years > 1) "s" else ""}"
                }
                else -> {
                    "$remainingMonths month${if (remainingMonths > 1) "s" else ""}"
                }
            }
        }



        fun formatDate(year: Int, month: Int, day: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun parseDateToMillis(dateString: String, dateFormat: String = "dd/MM/yyyy"): Long {
            return if (dateString.isNotEmpty()) {
                try {
                    // Define the date format
                    val format = SimpleDateFormat(dateFormat, Locale.getDefault())
                    val date = format.parse(dateString)
                    date?.time ?: System.currentTimeMillis() // Return the date in milliseconds or current time if parsing fails
                } catch (e: Exception) {
                    e.printStackTrace()
                    System.currentTimeMillis() // Fallback to current time if there's an error in parsing
                }
            } else {
                System.currentTimeMillis() // Return current time if input is empty
            }
        }

        fun parseMillisToDate(millis: Long?, dateFormat: String = "yyyy-MM-dd"): String {
            requireNotNull(millis) { "Millis value cannot be null" }
            try {
                // Define the date format
                val format = SimpleDateFormat(dateFormat, Locale.getDefault())
                val date = Date(millis) // Convert milliseconds to Date object
                return format.format(date) // Format the Date object into a string
            } catch (e: Exception) {
                e.printStackTrace()
                return "" // Return an empty string if there's an error
            }
        }
        fun addMonthsToMillis(millis: Long, monthsToAdd: Int): Long {
            // Convert milliseconds to LocalDate

            val date = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            // Add the months to the LocalDate
            val newDate = date.plusMonths(monthsToAdd.toLong())

            // Convert the updated LocalDate back to milliseconds
            return newDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        fun hashPassword(password: String): String {
            return BCrypt.hashpw(password, BCrypt.gensalt())
        }
        fun checkPassword(enteredPassword: String, storedHash: String): Boolean {
            return BCrypt.checkpw(enteredPassword, storedHash)
        }


    }
}
