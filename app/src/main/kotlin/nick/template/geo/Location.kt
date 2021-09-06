package nick.template.geo

sealed class Location {
    object Unknown : Location()
    data class Found(
        val address: String,
        val city: String,
        val area: String
    ) : Location()
}
