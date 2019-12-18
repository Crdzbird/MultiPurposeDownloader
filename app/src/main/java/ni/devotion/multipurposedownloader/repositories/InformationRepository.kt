package ni.devotion.multipurposedownloader.repositories

import ni.devotion.multipurposedownloader.data.network.interfaces.InformationInterface
import ni.devotion.multipurposedownloader.models.Information

interface InformationRepository {
    suspend fun obtainInformation(): List<Information>
}

class InformationRepositoryImpl(private val informationService: InformationInterface): InformationRepository{
    override suspend fun obtainInformation() = informationService.requestCountry()
}