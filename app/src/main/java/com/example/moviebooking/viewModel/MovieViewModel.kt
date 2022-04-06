package com.example.moviebooking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebooking.common.Constants
import com.example.moviebooking.interfaces.ApiInterface
import com.example.moviebooking.interfaces.FirebaseInterface
import com.example.moviebooking.models.Movie
import com.example.moviebooking.models.Result
import com.example.moviebooking.models.SeatData
import com.example.moviebooking.models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieViewModel: ViewModel() {

    var allMoviesData = MutableLiveData<ArrayList<Result>>()
    var loggedInUserId = MutableLiveData<String>()
    var loggedInUserDetails = MutableLiveData<UserData>()

    fun getMovies() {
        val apiInterface = ApiInterface.getRetrofitObject().create(ApiInterface::class.java)
        val retrofitData = apiInterface.getData(Constants.API_KEY)
        var moviesList = ArrayList<Result>()

        retrofitData.enqueue(object : Callback<Movie?> {
            override fun onResponse(call: Call<Movie?>, response: Response<Movie?>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    moviesList = responseBody.results as ArrayList<Result>
                }
                allMoviesData.value = moviesList
            }

            override fun onFailure(call: Call<Movie?>, t: Throwable) {  }
        })
    }

    fun createUser(firstName: String, lastName: String, email: String, password: String, completion: FirebaseInterface) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idOfUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
                loggedInUserId.value = idOfUser
                val userData = UserData(firstName, lastName, email)
                FirebaseFirestore.getInstance().collection(Constants.USER_DATA_KEY).document(idOfUser).set(userData).addOnSuccessListener {
                    getLoggedInUserData(completion)
                }.addOnFailureListener { exception ->
                    completion.onFailure(exception.toString())
                }
            }
        }.addOnFailureListener { exception ->
            completion.onFailure(exception.toString())
        }
    }

    fun signInUser(email: String, password: String, completion: FirebaseInterface) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idOfUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
                loggedInUserId.value = idOfUser
                getLoggedInUserData(completion)
            }
        }.addOnFailureListener { exception ->
            completion.onFailure(exception.toString())
        }
    }

    private fun getLoggedInUserData(completion: FirebaseInterface) {
        var userData: UserData?
        loggedInUserId.value?.let { id ->
            FirebaseFirestore.getInstance().collection(Constants.USER_DATA_KEY).document(id).get().addOnSuccessListener {
                userData = it.toObject(UserData::class.java)
                loggedInUserDetails.value = userData ?: UserData()
                completion.onSuccess(userData)
            }
        }
    }

    fun getSeatStatus(selectedMovie: String, completion: (List<String>?) -> Unit) {
        var seatArray: SeatData?
        FirebaseFirestore.getInstance().collection(Constants.MOVIE_KEY).document(selectedMovie).get().addOnSuccessListener {
            seatArray = it.toObject(SeatData::class.java)
            completion(seatArray?.bookedSeatArray)
        }
    }
}