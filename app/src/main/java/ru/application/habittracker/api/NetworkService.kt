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


//    @Headers(
//        "accept: application/json",
//        "Authorization: a486867e-cc2c-41de-953e-3ba7542c1ae1"
//    )

//    @FormUrlEncoded
//    @POST("habit_done")
//    fun addHabit(
//        @Field("json") id: String
//    ): Call<String>


    @Headers(
        "accept: application/json",
        "Authorization: a486867e-cc2c-41de-953e-3ba7542c1ae1"
    )

    @FormUrlEncoded
    @PUT("habit")
    fun addHabit(
        @Field("json") id: String
    ): Call<String>

    @DELETE("habit")
    fun deleteHabit(): Call<String>


}