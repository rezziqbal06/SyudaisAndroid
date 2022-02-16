/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.su.service

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.su.service.data.ArtikelRepository
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.local.SyudaisDatabase
import com.su.service.data.source.remote.ArtikelService
import com.su.service.data.source.remote.DoaService
import com.su.service.data.source.remote.DzikirService
import com.su.service.ui.artikel.ArtikelViewModelFactory
import com.su.service.ui.doa.DoaRepository
import com.su.service.ui.doa.DoaViewModelFactory
import com.su.service.ui.dzikir.DzikirRepository
import com.su.service.ui.dzikir.DzikirViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    fun provideCache(context: Context): SyudaisCache {
        val database = SyudaisDatabase.getInstance(context)
        return SyudaisCache(database.syudaisDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideArtikelRepository(context: Context): ArtikelRepository {
        return ArtikelRepository(ArtikelService.create(), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ArtikelViewModelFactory(provideArtikelRepository(context))
    }

    //Doa
    private fun provideDoaCache(context: Context): SyudaisCache {
        val database = SyudaisDatabase.getInstance(context)
        return SyudaisCache(database.syudaisDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideDoaRepository(context: Context): DoaRepository {
        return DoaRepository(DoaService.create(), provideDoaCache(context))
    }

    fun provideDoaViewModelFactory(context: Context): ViewModelProvider.Factory {
        return DoaViewModelFactory(provideDoaRepository(context))
    }

    //Dzikir
    fun provideDzikirCache(context: Context): SyudaisCache {
        val database = SyudaisDatabase.getInstance(context)
        return SyudaisCache(database.syudaisDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideDzikirRepository(context: Context): DzikirRepository {
        return DzikirRepository(DzikirService.create(), provideDzikirCache(context))
    }

    fun provideDzikirViewModelFactory(context: Context): ViewModelProvider.Factory {
        return DzikirViewModelFactory(provideDzikirRepository(context))
    }
}
