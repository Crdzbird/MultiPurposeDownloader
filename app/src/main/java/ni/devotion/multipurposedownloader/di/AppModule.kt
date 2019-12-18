package ni.devotion.multipurposedownloader.di

import ni.devotion.multipurposedownloader.data.network.remoteDataSourceModule
import ni.devotion.multipurposedownloader.repositories.InformationRepository
import ni.devotion.multipurposedownloader.repositories.InformationRepositoryImpl
import ni.devotion.multipurposedownloader.ui.viewModel.InformationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { InformationViewModel(get()) }

    single<InformationRepository> {
        InformationRepositoryImpl(get())
    }
}

val allAppModules = listOf(appModule, remoteDataSourceModule)