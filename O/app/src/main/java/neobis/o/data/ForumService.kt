package neobis.o.data

import neobis.o.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ForumService {

    // Получить список Постов
    @GET("posts")
    fun loadPosts(): Call<MutableList<Post>>

    // Получить список комментариев
    @GET("comments")
    fun loadComments(): Call<List<Comment>>

    // Получить список комментариев Поста
    @GET("posts/{postId}/comments")
    fun loadPostComments(@Path("postId") postid: Int): Call<List<Comment>>

    // Получить список альбомов
    @GET("albums")
    fun loadAlbums(): Call<List<Album>>

    // Получить список фотографий альбома
    @GET("albums/{albumId}/photos")
    fun loadAlbumPhotos(@Path("albumId") albumId: Int): Call<List<Photos>>

    @GET("data/2.5/weather")
    fun getWeatherForCity(@Query("id") cityId: Int, @Query("appid") token: String): Call<WeatherInfo>
}