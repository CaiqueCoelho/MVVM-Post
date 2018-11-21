package coelho.caique.mvvmposts.data.network

import coelho.caique.mvvmposts.data.model.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {

    /**
     * Get the list of the pots from the API
     */
    @GET("/posts")
    fun getPosts(): Observable<List<Post>>

}