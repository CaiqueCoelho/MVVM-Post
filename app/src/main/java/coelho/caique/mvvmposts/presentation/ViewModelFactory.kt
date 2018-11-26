package coelho.caique.mvvmposts.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import coelho.caique.mvvmposts.data.remote.AppDatabase

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "posts").build()
            @Suppress("UNCHECKED_CAST")
            return PostListViewModel(db.postDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}