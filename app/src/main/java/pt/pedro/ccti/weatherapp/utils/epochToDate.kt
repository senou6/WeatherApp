package pt.pedro.ccti.weatherapp.utils

import java.util.Calendar

fun epochToWeekDay(epoch: Long): String {
    val calendar = Calendar.getInstance()
    // Convert epoch to milliseconds
    calendar.timeInMillis = epoch * 1000

    val todayCalendar = Calendar.getInstance()

    // Check for 'Today'
    if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
        calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
        return "Today"
    }

    // Check for 'Yesterday'
    todayCalendar.add(Calendar.DAY_OF_YEAR, -1)
    if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
        calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
        return "Yesterday"
    }

    // Check for 'Tomorrow'
    todayCalendar.add(Calendar.DAY_OF_YEAR, 2) // We already subtracted 1, so we add 2
    if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
        calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
        return "Tomorrow"
    }

    // If not today, yesterday, or tomorrow, then find the specific day of the week
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        Calendar.SUNDAY -> "Sunday"
        else -> "Unknown"
    }
}