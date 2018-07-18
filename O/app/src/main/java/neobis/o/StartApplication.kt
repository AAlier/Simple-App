package neobis.o

import android.app.Application
import neobis.o.data.ForumService
import neobis.o.data.Network
import neobis.o.utils.Constant

class StartApplication : Application() {
    companion object {
        @Volatile
        lateinit var INSTANCE: StartApplication
        lateinit var service: ForumService
        lateinit var weatherService: ForumService
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        service = Network.initService(Constant.BASE_URL)
        weatherService = Network.initService(Constant.WEATHER_URL)
    }
}
/**
Использовать два апи:
1. С сайта http://jsonplaceholder.typicode.com/
2. Любое погодное апи.

Сделать на основе апи, простое приложение отображающее получаемые данные.

Использовать нижнюю навигацию между двумя главными экранами: Посты и Альбомы

Посты
Получить список Постов. Выбрать случайным образом 10 из них. Получить список комментариев.
Выбрать только те комментарии, которые относятся к 10 ранее выбранным постам.
На экране отображать вперемешку список постов и прогнозов погоды в Бишкеке,
Чолпон-Ате, Нарыне, Оше. При тапе на пост, открывать экран со списком комментариев.
При тапе на прогноз погоды открывать сайт o.kg

Альбомы
Получить список альбомов. Случайным образом выбрать 10 из них.
Для каждого из албомов получить список фотографий используя API.


Использовать нижнюю навигацию. На первом экране отображать посты списком.
при входе отображать комментарии списком. На втором экране отображать
альбомы сеткой, при входе в альбом отображать картинки сеткой.
При тапе на картинку открывать ее на весь экран.
 */