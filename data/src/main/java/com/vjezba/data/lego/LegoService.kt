package com.vjezba.data.lego

import com.vjezba.data.database.model.LegoTheme
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Lego REST API access points
 */
interface LegoService {

    companion object {
        const val ENDPOINT = "https://rebrickable.com/api/v3/"
    }


    @GET("lego/themes/")
    suspend fun getThemes(@Query("page") page: Int? = null,
                          @Query("page_size") pageSize: Int? = null,
                          @Query("ordering") order: String? = null): Response<ResultsResponse<LegoTheme>>

}
