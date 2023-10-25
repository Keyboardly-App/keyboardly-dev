package app.keyboardly.addon.sample.di

import app.keyboardly.addon.sample.data.local.SampleDbManager
import app.keyboardly.addon.sample.data.remote.ProvinceRepository
import app.keyboardly.addon.sample.data.remote.ProvinceService
import org.koin.dsl.module

/**
 * Created by zainal on 23/10/22 - 9:30 AM
 */


val sampleModule = module {
    factory { ProvinceService.client(get()) }
    single { SampleDbManager.getInstance(get()) }
    factory { ProvinceRepository(get(), get()) }
}