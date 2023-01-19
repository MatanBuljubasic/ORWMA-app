package com.example.orwma_proj_zad

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MatchesFragment : Fragment(), MatchRecyclerAdapter.ContentListener {
    private lateinit var recyclerAdapter: MatchRecyclerAdapter
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_matches, container, false)

        val service = ServiceBuilder.buildService(RapidApiEndpoints::class.java)
        val sportKey = arguments?.getString("SPORT")
        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        val sportsFragment = SportsFragment()
        val favoriteButton = view.findViewById<Button>(R.id.goToFavouritesButton)
        val favoriteFragment = FavoritesFragment()

        backButton.setOnClickListener {
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, sportsFragment)
            fragmentTransaction?.commit()
        }

        favoriteButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("SPORT", sportKey)
            favoriteFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, favoriteFragment)
            fragmentTransaction?.commit()
        }

        sportKey?.let { service.getMatches(it) }?.enqueue(object : Callback<ArrayList<Match>> {
            override fun onResponse(
                call: Call<ArrayList<Match>>,
                response: Response<ArrayList<Match>>
            ) {
                if(response.isSuccessful){
                    val matchesList = view.findViewById<RecyclerView>(R.id.matchesList)
                    recyclerAdapter = MatchRecyclerAdapter(response.body()!!, this@MatchesFragment)
                    matchesList.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = recyclerAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Match>>, t: Throwable) {
                Log.e("MatchesFragment", t.message.toString())
            }

        })


        return view
    }


    override fun onItemButtonClick(index: Int, match: Match, clickType: MatchClick) {
        if(clickType == MatchClick.FAVORITE){
            val list = ArrayList<Match>()
            db.collection("matches").get().addOnSuccessListener { it ->
                for (data in it.documents) {
                    val match = data.toObject(Match::class.java)
                    if (match != null) {
                        list.add(match)
                    }
                }
                val addedMatch: Match? = list.firstOrNull{it.id == match.id}
                if(addedMatch != null){
                    Toast.makeText(context, "Match already in favorites", Toast.LENGTH_SHORT).show()
                } else {
                    db.collection("matches").add(match)
                    Toast.makeText(context, "Match added to favorites", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Log.e("MatchesFragment", it.message.toString())
            }

        }
    }


}