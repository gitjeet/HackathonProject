package com.example.health

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.google.firebase.firestore.FirebaseFirestore
import com.huawei.hihealth.error.HiHealthError
import com.huawei.hihealth.listener.ResultCallback
import com.huawei.hihealthkit.HiHealthDataQuery
import com.huawei.hihealthkit.HiHealthDataQueryOption
import com.huawei.hihealthkit.auth.HiHealthAuth
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType
import com.huawei.hihealthkit.data.HiHealthPointData
import com.huawei.hihealthkit.data.HiHealthSetData
import com.huawei.hihealthkit.data.store.HiHealthDataStore
import com.huawei.hihealthkit.data.store.HiRealTimeListener
import com.huawei.hihealthkit.data.type.HiHealthPointType
import com.huawei.hihealthkit.data.type.HiHealthSetType
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ExcersiceFragment : Fragment() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var btn11: Button? = null
    private var btn13: Button? = null

    private lateinit var queryCountStepNumber: TextView



    private lateinit var qeuryImageView: ImageView
    private lateinit var calorieImageView: ImageView
    private lateinit var textCalorieTextView: TextView
    private lateinit var sleepImageView: ImageView
    private lateinit var sleepTextView: TextView
    private lateinit var spo2ImageView: ImageView
    private lateinit var spo2TextView: TextView
    private lateinit var heartbeatImageView: ImageView
    private lateinit var heartTextView: TextView
    private lateinit var button_start: Button
    private lateinit var button_stop: Button
    private lateinit var requestDataButton: Button
    private lateinit var uploadDataButton: Button
    private lateinit var db :FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_excersice, container, false)



        btn11 = view.findViewById(R.id.btn_click_11)
        calorieImageView = view.findViewById(R.id.calorie)
        textCalorieTextView = view.findViewById(R.id.txt_calorie)
        queryCountStepNumber = view.findViewById(R.id.txt_query_steps)
        qeuryImageView = view.findViewById(R.id.query)
        sleepImageView = view.findViewById(R.id.sleep)
        sleepTextView = view.findViewById(R.id.txt_sleep)
        spo2TextView = view.findViewById(R.id.txt_spo2)
        spo2ImageView = view.findViewById(R.id.spo2)
        heartbeatImageView = view.findViewById(R.id.iv_heart)
        heartTextView = view.findViewById(R.id.txt_heart_rate)
        button_start = view.findViewById(R.id.btn_start)
        button_stop = view.findViewById(R.id.btn_stop)
        requestDataButton = view.findViewById(R.id.btn_data)
        uploadDataButton = view.findViewById(R.id.btn_upload)
        db =FirebaseFirestore.getInstance()


        val circularProgressBar = view.findViewById<CircularProgressBar>(R.id.circularProgressBar_calorie)
        requestDataButton.performClick()



        requestDataButton.setOnClickListener {


            val button_start = view.findViewById<View>(R.id.btn_start)
            button_start.performClick()



            val queryCounter = view.findViewById<View>(R.id.query)
            queryCounter.performClick()

            val calorieCounter = view.findViewById<View>(R.id.calorie)
            calorieCounter.performClick()

            val sleepCounter = view.findViewById<View>(R.id.sleep)
            sleepCounter.performClick()

            val spo2couter = view.findViewById<View>(R.id.spo2)
            spo2couter.performClick()


            uploadDataButton.visibility = Button.VISIBLE

            val uploadButton = view.findViewById<View>(R.id.btn_upload)
            uploadButton.performClick()



        }


        uploadDataButton.setOnClickListener {
            val result =
                "Your Hear Rate is " + heartTextView.text.toString() +
                        " Your Spo2 is " + spo2TextView.text.toString() +
                        " Your Sleep is " + sleepTextView.text.toString() +
                        " Your Calorie Burn is " + textCalorieTextView.text.toString() +
                        " Your Query and Count Steps Are" +   queryCountStepNumber.text.toString()
            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))

            setFragmentResult(
                "data_request_key",
                bundleOf("frag1_data_key" to heartTextView.text)
            )




            var hearrate:String =heartTextView.text.toString()
            var spo2: String = spo2TextView.text.toString()
            var sleep : String = sleepTextView.text.toString()
            var calorie: String = textCalorieTextView.text.toString()
            var query : String = queryCountStepNumber.text.toString()

            val uploadData = UploadData(hearrate, spo2, sleep, calorie, query)
            db.collection("health")
                .add(uploadData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}") }
                .addOnFailureListener{ e->
                    Log.w(TAG, "Error Adding Document ", e)
                }



        }

        btn11!!.setOnClickListener {
            val userAllowTypesToRead = intArrayOf(
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_USER_PROFILE_INFORMATION,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_USER_PROFILE_FEATURE,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_STEP_SUM,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_RUN_METADATA,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_WEIGHT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_REALTIME_HEARTRATE,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_REAL_TIME_SPORT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_WRITE_DATA_SET_WEIGHT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_CALORIES_SUM,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_CORE_SLEEP,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_LAST_OXYGEN_SATURATION
            )
            val userAllowTypesToWrite =
                intArrayOf(HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_WRITE_DATA_SET_WEIGHT)
            HiHealthAuth.requestAuthorization(
                context, userAllowTypesToWrite, userAllowTypesToRead
            ) { resultCode, `object` ->
                Log.i(
                    TAG,
                    "requestAuthorization onResult:$resultCode"
                )
                if (resultCode == HiHealthError.SUCCESS) {
                    Log.i(
                        TAG,
                        "requestAuthorization success resultContent:$`object`"
                    )
                }

            }
        }



        circularProgressBar.apply {
            var calorie_for_circular :Float =0f

            // Set Progress

            // or with animation
            setProgressWithAnimation(calorie_for_circular, 1000) // =1s

            // Set Progress Max
            progressMax = 3000f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.YELLOW
            progressBarColorDirection = CircularProgressBar.GradientDirection.BOTTOM_TO_END

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.YELLOW
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 17f // in DP
            backgroundProgressBarWidth = 13f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        val circularProgressBar_steps = view.findViewById<CircularProgressBar>(R.id.circularProgressBar_steps)
        circularProgressBar_steps.apply {

            var calorie_for_circular_steps :Float =0f





            // Set Progress
            progress = calorie_for_circular_steps
            // or with animation
            setProgressWithAnimation(calorie_for_circular_steps, 1000) // =1s

            // Set Progress Max
            progressMax = 3000f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.GREEN
            progressBarColorDirection = CircularProgressBar.GradientDirection.BOTTOM_TO_END

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.GREEN
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 17f // in DP
            backgroundProgressBarWidth = 13f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        val circularProgressBar_sleep = view.findViewById<CircularProgressBar>(R.id.circularProgressBar_sleep)
        circularProgressBar_sleep.apply {
            var calorie_for_circular_sleep :Float =0f

            // Set Progress
            progress = calorie_for_circular_sleep
            // or with animation
            setProgressWithAnimation(calorie_for_circular_sleep, 1000) // =1s

            // Set Progress Max
            progressMax = 7f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.BLACK
            progressBarColorDirection = CircularProgressBar.GradientDirection.BOTTOM_TO_END

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.BLACK
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 17f // in DP
            backgroundProgressBarWidth = 13f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        val circularProgressBar_spo2 = view.findViewById<CircularProgressBar>(R.id.circularProgressBar_spo2)
        circularProgressBar_spo2.apply {
            var calorie_for_circular_spo2 :Float =0f


            // Set Progress
            progress = calorie_for_circular_spo2
            // or with animation
            setProgressWithAnimation(calorie_for_circular_spo2, 1000) // =1s

            // Set Progress Max
            progressMax = 100f

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = Color.RED
            progressBarColorDirection = CircularProgressBar.GradientDirection.BOTTOM_TO_END

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = Color.RED
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 17f // in DP
            backgroundProgressBarWidth = 13f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        qeuryImageView.setOnClickListener {



            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000L * 60 * 60 * 24 * 30L // Check Data of the latest 30 days
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_STEP_SUM,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.getCount(
                context, hiHealthDataQuery
            ) { resultCode, data ->
                queryCountStepNumber.text = data.toString() +" steps"

                if (resultCode == HiHealthError.SUCCESS) {
                    val count = data as Int
                    circularProgressBar_steps.progress = count.toFloat()
                    Log.i(
                        TAG,
                        "walk track number: $count"

                    )
                }
            }

        }





        calorieImageView.setOnClickListener {




            val timeout = 0
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000 * 60 * 60 * 24L
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_CALORIES_SUM,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                context, hiHealthDataQuery, timeout
            ) { resultCode, data ->

                if (resultCode == HiHealthError.SUCCESS) {
                    val dataList: List<*> = data as ArrayList<*>
                    if (dataList.size >= 1) {
                        val pointData = dataList[dataList.size - 1] as HiHealthPointData
                         textCalorieTextView.text = pointData.value.toString() + " cal"
                        circularProgressBar.progress=pointData.value.toFloat()

                    }

                }

            }
        }

        sleepImageView.setOnClickListener {




            val timeout = 0

            // Set the start time and end time for the query.
            // Set the start time and end time for the query.
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 86400000L * 30
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthSetType.DATA_SET_CORE_SLEEP,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                context,
                hiHealthDataQuery,
                timeout,
                object : ResultCallback {
                    val sb = StringBuilder()
                    override fun onResult(resultCode: Int, data: Any) {
                        if (resultCode == HiHealthError.SUCCESS) {
                            var test: Float =
                                (HiHealthSetType.CONTENT_SLEEP_BED_TIME.toFloat() / 10000)
                            Log.i(TAG, "query not null,enter set data $test")
                            val dataList = data as List<*>
                            circularProgressBar_sleep.progress = test
                            if (dataList.size >= 1) {
                                val hiHealthData =
                                    dataList[dataList.size - 1] as HiHealthSetData

                                val map = hiHealthData.map
                                sb.append("" + map[HiHealthSetType.CONTENT_SLEEP_WHOLE_DAY_AMOUNT])
                            }
                            sleepTextView.text = String.format("%.1f", test) + "hr"
                        }
                    }
                })
        }

        spo2ImageView.setOnClickListener {



            val timeout = 0
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000 * 60 * 60 * 24 * 30L
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_LAST_OXYGEN_SATURATION,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                context, hiHealthDataQuery, timeout
            )
            { resultCode, data ->
                Log.i(
                    TAG,
                    "query steps resultCode: $resultCode"
                )
                val count = 47204
                var result = 0
                if (resultCode == HiHealthError.SUCCESS) {
                    result = (HiHealthPointType.DATA_POINT_LAST_OXYGEN_SATURATION)

                }
                Log.e(TAG, "result is adfad $result")
                spo2TextView.text = ((count / result) * 100).toString() +"%"
                circularProgressBar_spo2.progress = ((count / result) * 100).toFloat()
            }


        }

        var heartCallback: HiRealTimeListener = object : HiRealTimeListener {
            override fun onResult(state: Int) {
                Log.i(TAG, " onResult state:$state")

            }

            override fun onChange(resultCode: Int, value: String) {
                val sb = StringBuilder()
                Log.i(
                    TAG,
                    " onChange resultCode: $resultCode value: $value"
                )
                if (resultCode == HiHealthError.SUCCESS) {
                    try {
                        val jsonObject = JSONObject(value)
                        sb.append("" + jsonObject.getInt("hr_info"))

                    } catch (e: JSONException) {
                        Log.e(TAG, "JSONException e" + e.message)
                    }

                }

                heartTextView.text = sb.toString() + " BPM"



            }
        }
        heartbeatImageView.setOnClickListener {
            heartbeatImageView.clearAnimation()
        }
        button_start.setOnClickListener {

            heartbeatImageView.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.heartbeat
                )
            )
            HiHealthDataStore.startReadingHeartRate(
                context,
                heartCallback
            )


        }
        button_stop.setOnClickListener {

            HiHealthDataStore.stopReadingHeartRate(
                context,
                heartCallback
            )
            heartbeatImageView.clearAnimation()

        }


        return view

    }


}
