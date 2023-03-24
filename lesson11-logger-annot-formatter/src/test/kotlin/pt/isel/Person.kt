package pt.isel

import java.time.LocalDate

class Person (
    val name: String,
    @Format(formatter = FormatStringToUpper::class) val address: String,
    @Format(formatter = FormatDateAsWeekOfYear::class) val birth: LocalDate
)