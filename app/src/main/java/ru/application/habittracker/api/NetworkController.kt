package ru.application.habittracker.api

import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.data.FeedDao
import java.util.*
import kotlin.collections.ArrayList


class NetworkController {

    // Получить данные из сети
    fun netWorkGet(dao: FeedDao) {
        GlobalScope.launch(Dispatchers.Default) {
            // запрос
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_NETWORK)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(NetworkService::class.java)
            val habits: Call<List<HabitItem>> = service.listHabits()

            // ответ
            @Suppress("BlockingMethodInNonBlockingContext") val response = habits.execute()
            val code = response.code()
            val message = response.body() ?: ArrayList()

            // добавить привычки в БД
            if (code == 200) {
                if (message.isNotEmpty()) {
                    for (habit in message) {
                        // Если на сервере есть данные, загрузить в базу / обновить в базе
                        if (dao.findById(habit.id) == null) {
                            dao.insert(habit)
                        } else {
                            dao.update(habit)
                        }
                    }
                } else {
                    Log.e("GET", "Нет данных по API, код ответа: $code")
                }
            } else {
                Log.e("GET", "Ошибка выгрузки данных по API, код ответа: $code")
            }
        }
    }


    // Признак новой привычки
    fun netWorkPost(habit: HabitItem) {
        GlobalScope.launch(Dispatchers.Default) {
            val gson = GsonBuilder()
                .registerTypeAdapter(HabitItem::class.java, JsonPostModelSerializer())
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_NETWORK)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val service = retrofit.create(NetworkService::class.java)
            val addHabit: Call<Void> = service.addHabit(JsonPostModel(1, habit.id))

            @Suppress("BlockingMethodInNonBlockingContext") val response = addHabit.execute()

            if (!response.isSuccessful) {
                Log.e("error", "Ошибка добавления привычки с id=${habit.id}, код ответа: ${response.code()}")
            } else {
                netWorkPut(habit)
            }
        }
    }


    // Загрузить данные из БД на сервер
    fun netWorkPut(habit: HabitItem) {
        GlobalScope.launch(Dispatchers.Default) {

            val gson = GsonBuilder()
                .registerTypeAdapter(HabitItem::class.java, HabitJsonSerializer())
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_NETWORK)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val service = retrofit.create(NetworkService::class.java)
            val createHabit: Call<HabitItem> = service.createHabit(habit)

            // запрос
            createHabit.request()

            // ответ
            @Suppress("BlockingMethodInNonBlockingContext")
            val response = createHabit.execute()

            if (!response.isSuccessful) {
                Log.e("error", "Ошибка обновления привычки с id=${habit.id}, код ответа: ${response.code()}")
            }
        }
    }


    // Удалить данные по API
    fun netWorkDelete(id: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val gson = GsonBuilder()
                .registerTypeAdapter(HabitItem::class.java, JsonDeleteModelSerializer())
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_NETWORK)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val service = retrofit.create(NetworkService::class.java)
            val deleteHabit: Call<Void> = service.deleteHabit(JsonDeleteModel(id))

            @Suppress("BlockingMethodInNonBlockingContext") val response = deleteHabit.execute()

            if (!response.isSuccessful) {
                Log.e("error", "Ошибка удаления привычки с id=$id, код ответа: ${response.code()}")
            } else {
                Log.e("success", "Удачное удаление с id=$id, код ответа: ${response.code()}")
            }
        }
    }
}