package com.vjezba.data.repository

import com.vjezba.data.lego.BaseDataSource
import com.vjezba.data.lego.LegoService
import javax.inject.Inject

/**
 * Works with the Lego API to get data.
 */
class LegoSetRemoteDataSource @Inject constructor(private val service: LegoService) : BaseDataSource() {

    suspend fun fetchSets(page: Int, pageSize: Int? = null, themeId: Int? = null)
            = getResult { service.getSets(page, pageSize, themeId, "-year") }

    /*suspend fun fetchSet(id: String)
            = getResult { service.getSet(id) }*/
}
