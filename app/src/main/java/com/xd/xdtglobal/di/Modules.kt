package com.xd.xdtglobal.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.xd.xdtglobal.audio.ScoreBuilder
import com.xd.xdtglobal.data.GameRepo
import com.xd.xdtglobal.data.GameRepoImpl
import com.xd.xdtglobal.data.gameDataStore
import com.xd.xdtglobal.game.logic.DeviceSignalsProvider
import com.xd.xdtglobal.game.logic.ResolveStartFlowUseCase
import com.xd.xdtglobal.game.logic.SavedScoreRouter
import com.xd.xdtglobal.game.logic.ScoreParamsCollector
import com.xd.xdtglobal.viewmodel.StartViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val gameModule = module {

    single<GameRepo> {
        GameRepoImpl(
            dataStore = get()
        )
    }

    single {
        ScoreBuilder()
    }

    single {
        SavedScoreRouter()
    }

    single<GameRepo> {
        GameRepoImpl(get())
    }

    single {
        DeviceSignalsProvider(
            context = androidContext()
        )
    }

    single {
        ScoreParamsCollector(
            signalsProvider = get()
        )
    }

    factory {
        ResolveStartFlowUseCase(
            gameRepo = get(),
            paramsCollector = get(),
            linkBuilder = get(),
            savedScoreRouter = get()
        )
    }

    viewModel {
        StartViewModel(
            resolveStartFlowUseCase = get()
        )
    }
}

val dataStoreModule = module {
    single<DataStore<Preferences>> {
        androidContext().gameDataStore
    }
}