package pt.pedro.ccti.weatherapp.model.User

data class MUser(
    val id: String?,
    val userId: String,
    val username: String,
    val favoriteLocations: List<String>,
    ){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "username" to this.username,
            "favorite_locations" to this.favoriteLocations
        )
    }
}
