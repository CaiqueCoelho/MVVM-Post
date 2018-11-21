package coelho.caique.mvvmposts.presentation

import android.arch.lifecycle.ViewModel
import coelho.caique.mvvmposts.DaggerViewModelInjector
import coelho.caique.mvvmposts.NetworkModule
import coelho.caique.mvvmposts.ViewModelInjector

abstract class BaseViewModel : ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> injector.inject(this)
        }
    }

}