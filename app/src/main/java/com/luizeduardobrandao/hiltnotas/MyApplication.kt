package com.luizeduardobrandao.hiltnotas

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// * Classe Application que inicializa o Hilt no nível da aplicação.
@HiltAndroidApp
class MyApplication: Application() {
}