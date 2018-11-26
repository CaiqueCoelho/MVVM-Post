package coelho.caique.mvvmposts.presentation

import android.arch.lifecycle.MutableLiveData
import android.view.View
import coelho.caique.mvvmposts.R
import coelho.caique.mvvmposts.data.model.Local.Post
import coelho.caique.mvvmposts.data.network.ApiService
import coelho.caique.mvvmposts.data.remote.PostDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostListViewModel(private val postDao: PostDao) : BaseViewModel() {

    @Inject
    lateinit var apiService: ApiService

    private lateinit var subscription: Disposable
    val postListAdapter: PostListAdapter = PostListAdapter()

    val errorMessage:MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    init{
        loadPosts()
    }

    private fun loadPosts(){
        subscription = Observable.fromCallable { postDao.all }
            .concatMap {
                    dbPostList ->
                if(dbPostList.isEmpty())
                    apiService.getPosts().concatMap {
                            apiPostList -> postDao.insertAll(*apiPostList.toTypedArray())
                        Observable.just(apiPostList)
                    }
                else
                    Observable.just(dbPostList)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                // Add result
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }
            )
    }

    private fun onRetrievePostListStart(){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(result: List<Post>) {
        postListAdapter.updatePostList(result)
    }

    private fun onRetrievePostListError(){
        errorMessage.value = R.string.post_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}