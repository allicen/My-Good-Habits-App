package ru.application.habittracker.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.application.habittracker.core.Constants
import ru.application.habittracker.core.HabitItem
import ru.application.habittracker.data.FeedDao


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

            // добавить в БД
            if (code == 200) {
                if (message.isNotEmpty()) {
                    for (habit in message) {
                        val jsonObject = JsonObject()
                        val gson = Gson()
                        val newHabit = gson.fromJson<HabitItem>(jsonObject, HabitItem::class.java)
                        dao.insert(newHabit)
                    }
                } else {
                    Log.e("GET", "Нет данных по API")
                }
            } else {
                Log.e("GET", "Ошибка выгрузки данных по API")
            }
        }
    }


    // Отправить данные в сеть
    fun netWorkPost(data: HabitItem) {
        GlobalScope.launch(Dispatchers.Default) {

            val postJsonString: String = GsonBuilder().create().toJson("{\"date\":0,\"habit_uid\":\"string\"}")
            val JSON = "application/json; charset=utf-8".toMediaType()
            val client = OkHttpClient()
            val body = RequestBody.create(JSON, postJsonString)
            val request: Request = Request.Builder()
                .url(Constants.URL_NETWORK + "habit_done")
                .header("Authorization", "a486867e-cc2c-41de-953e-3ba7542c1ae1")
                .post(body)
                .build()

            @Suppress("BlockingMethodInNonBlockingContext")
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e("error", "Ошибка добавления привычки с id=${data.id}")
            }
        }
    }

    // Загрузить данные из БД на сервер
    fun netWorkPut(habit: HabitItem) {
        GlobalScope.launch(Dispatchers.Default) {

            val gson = GsonBuilder()
                .registerTypeAdapter(HabitItem::class.java, HabitJsonSerializer())
                .registerTypeAdapter(HabitItem::class.java, HabitJsonDeserializer())
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_NETWORK)
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
                Log.e("error", "Ошибка обновления привычки с id=${habit.id}")
            }
        }
    }

    // Удалить данные в сети
    fun netWorkDelete(id: Int) {
        GlobalScope.launch(Dispatchers.Default) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_NETWORK)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(NetworkService::class.java)
            val deleteHabit: Call<String> = service.deleteHabit(id.toString())

            // запрос
            val request = deleteHabit.request()

            // ответ
            @Suppress("BlockingMethodInNonBlockingContext")
            val response = deleteHabit.execute()

            if (!response.isSuccessful) {
                Log.e("error", "Ошибка удаления привычки с id=$id")
            }
        }
    }
}