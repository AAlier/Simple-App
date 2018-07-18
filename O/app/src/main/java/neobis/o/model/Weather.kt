package neobis.o.model

/**
 * Created by Alier on 18.07.2018.
 */
data class Weather(
        var id: Int,
        var main: String? = null,
        var description: String? = null,
        var icon: String? = null)