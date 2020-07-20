package ru.application.habittracker.api

import androidx.annotation.Keep
import retrofit2.Call
import retrofit2.http.*
import ru.application.habittracker.core.HabitItem


@Keep
interface NetworkService {

    @Headers(
        "accept: application/json",
        "Authorization: a486867e-cc2c-41de-953e-3ba7542c1ae1"
    )

    @GET("habit")
    fun listHabits(): Call<List<HabitItem>>

    @Headers(
        "accept: application/json",
        "Authorization: a486867e-cc2c-41de-953e-3ba7542c1ae1"
    )

    @POST("habit_done")
    fun addHabit(@Body requestBody: JsonPostModel) : Call<Void>

    @Headers(
        "accept: application/json",
        "Authorization: a486867e-cc2c-41de-953e-3ba7542c1ae1"
    )

    @PUT("habit")
    fun createHabit(@Body habit: HabitItem) : Call<HabitItem>


    @Headers(
        "accept: application/json",
        "Authorization: a486867e-cc2c-41de-953e-3ba7542c1ae1"
    )

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    fun deleteHabit(@Body requestBody: JsonDeleteModel): Call<Void>

}