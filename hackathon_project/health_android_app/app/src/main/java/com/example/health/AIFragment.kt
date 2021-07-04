package com.example.health

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class AIFragment : Fragment() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var aiImageView: ImageView
    private lateinit var ai_textview: TextView
    private lateinit var heart_rate_string: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a_i, container, false)


        aiImageView = view.findViewById(R.id.ai_btn)
        ai_textview = view.findViewById(R.id.ai_txt)

        heart_rate_string = " "
        aiImageView.setOnClickListener {



            setFragmentResultListener("data_request_key") { requestKey, bundle ->

                heart_rate_string = bundle.getString("frag1_data_key").toString()

            }








            heart_rate_string =heart_rate_string.replace("BPM","")




            ai_textview?.text = heart_rate_string
            val url = "https://apiherokuhealth.herokuapp.com/heart_beat_check"

            val params = HashMap<String,String>()
            params["age"] = "1"
            params["heartBeat"] = heart_rate_string
            val jsonObject = JSONObject(params as Map<*, *>)


            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                { response ->

                    // Process the json
                    try {
                        if(extractDefinationFromJson(response)=="1"){
                            ai_textview.text = "Healthy"
                        }
                        else{
                            ai_textview.text=" You Should Consult to Doctor "
                        }


                    } catch (e: Exception) {
                        ai_textview.text = heart_rate_string

                    }

                }, {
                    // Error in request
                    ai_textview.text = heart_rate_string
                })

            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            // Add the volley post request to the request queue
            activity?.let { it1 -> VolleySingleton.getInstance(it1).addToRequestQueue(request) }




        }




        return view
    }
    private fun extractDefinationFromJson(response: JSONObject): String {

        val health_validate =  response.getString("Result")
        Log.i(TAG,"HI")
        Log.i(TAG,"response result $health_validate")

        return health_validate
    }

}