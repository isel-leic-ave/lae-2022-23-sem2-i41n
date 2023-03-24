package pt.isel

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FormatDateAsWeekOfYear : Formatter {
    override fun format(dt: Any) : String {
        require(dt is LocalDate) { "This formatter only supports LocalDate arguments " }
        return dt.format(DateTimeFormatter.ISO_WEEK_DATE)
    }
}
