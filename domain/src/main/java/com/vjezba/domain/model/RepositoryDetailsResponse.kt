package com.vjezba.domain.model


data class RepositoryDetailsResponse(
    val id: Long = 0,
    val ownerApi: RepositoryOwnerResponse = RepositoryOwnerResponse(""),
    val name: String? = "",
    val description: String? = "",
    val html_url: String? = ""
)