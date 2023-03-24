package pt.isel

class FormatStringToUpper : Formatter {
    override fun format(s: Any) : String {
        require(s is String) { "This formatter only supports String arguments" }
        return s.uppercase()
    }
}
