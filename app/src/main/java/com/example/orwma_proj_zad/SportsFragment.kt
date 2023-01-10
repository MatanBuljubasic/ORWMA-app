package com.example.orwma_proj_zad

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsFragment : Fragment(), SportRecyclerAdapter.ContentListener {
    private lateinit var recyclerAdapter: SportRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_sports, container, false)


        val service = ServiceBuilder.buildService(RapidApiEndpoints::class.java)
        val call = service.getSports()


        call.enqueue(object: Callback<ArrayList<Sport>>{
            override fun onResponse(

                call: Call<ArrayList<Sport>>,
                response: Response<ArrayList<Sport>>
            ) {
                if(response.isSuccessful) {
                    val sportsList = view.findViewById<RecyclerView>(R.id.matchesList)
                    recyclerAdapter = SportRecyclerAdapter(response.body()!!, this@SportsFragment)
                    sportsList.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = recyclerAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Sport>>, t: Throwable) {
                Log.e("SportsFragment", t.message.toString())
            }

        })
        return view
    }

    override fun onItemButtonClick(sport: Sport, clickType: ItemClickType) {
        if(clickType == ItemClickType.SPORT){
            val bundle = Bundle()
            val fragmentNext = MatchesFragment()

            bundle.putString("SPORT", sport.key)
            fragmentNext.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, fragmentNext)
            fragmentTransaction?.commit()
        }
    }


}