package com.example.orwma_proj_zad

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesFragment : Fragment(), MatchRecyclerAdapter.ContentListener {
    private lateinit var recyclerAdapter: MatchRecyclerAdapter
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val service = ServiceBuilder.buildService(RapidApiEndpoints::class.java)
        val sportKey = arguments?.getString("SPORT")
        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        val matchesFragment = MatchesFragment()

        backButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("SPORT", sportKey)
            matchesFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, matchesFragment)
            fragmentTransaction?.commit()
        }

        sportKey?.let { service.getMatches(it) }?.enqueue(object : Callback<ArrayList<Match>>{
            override fun onResponse(
                call: Call<ArrayList<Match>>,
                response: Response<ArrayList<Match>>
            ) {
                if(response.isSuccessful){
                    val matchesList = view.findViewById<RecyclerView>(R.id.matchesList)
                    val favoriteMatchesList: ArrayList<Match> = ArrayList()
                    db.collection("matches").get().addOnSuccessListener { it ->
                        val list = ArrayList<Match>()
                        for( data in it.documents){
                            val match = data.toObject(Match::class.java)
                            if(match != null){
                                list.add(match)
                            }
                        }
                        for( match in list){
                            val favoriteMatch: Match? =
                                response.body()?.firstOrNull { it.id == match.id }
                            if (favoriteMatch != null) {
                                favoriteMatchesList.add(favoriteMatch)
                            }
                        }
                        recyclerAdapter = MatchRecyclerAdapter(favoriteMatchesList, this@FavoritesFragment)
                        matchesList.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = recyclerAdapter
                        }
                    }
                    .addOnFailureListener{
                        Log.e("FavoritesFragment", it.message.toString())
                    }


                }
            }

            override fun onFailure(call: Call<ArrayList<Match>>, t: Throwable) {
                Log.e("FavoritesFragment", t.message.toString())
            }

        })

        return view
    }



    override fun onItemButtonClick(index: Int, match: Match, clickType: MatchClick) {
        if(clickType == MatchClick.FAVORITE){
            recyclerAdapter.removeItem(index)
            db.collection("matches").document(match.id).delete()
        }
    }

}