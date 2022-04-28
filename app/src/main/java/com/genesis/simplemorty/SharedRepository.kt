package com.genesis.simplemorty

class SharedRepository {

    suspend fun getCharacterById(characterId: Int): GetCharacterByIdResponse? {
        val request = NetworkLayer.apiClient.getCharacterById(characterId)
        return when (request.isSuccessful) {
            true -> request.body()!!
            else -> null
        }
    }
}