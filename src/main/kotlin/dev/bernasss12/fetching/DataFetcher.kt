package dev.bernasss12.fetching

interface DataFetcher {
    suspend fun getModName(id: String): String
    suspend fun getSupportedGameVersions(id: String): List<String>
    suspend fun getDownloadCount(id: String): UInt
    suspend fun getSupportedModLoaders(id: String): List<String>
    suspend fun getLicence(id: String): String
}