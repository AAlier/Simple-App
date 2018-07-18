package neobis.o.model

/**
 * Created by Alier on 18.07.2018.
 */
data class WeatherInfo(var weather: List<Weather>? = null,
                       var main: Main? = null,
                       var id: Int,
                       var name: String? = null,
                       var cod: Int)