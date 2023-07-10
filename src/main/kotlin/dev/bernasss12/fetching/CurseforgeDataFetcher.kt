package dev.bernasss12.fetching

object CurseforgeDataFetcher : DataFetcher {

    override suspend fun getModName(id: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getSupportedGameVersions(id: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getDownloadCount(id: String): UInt {
        TODO("Not yet implemented")
    }

    override suspend fun getSupportedModLoaders(id: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getLicence(id: String): String {
        TODO("Not yet implemented")
    }

}
