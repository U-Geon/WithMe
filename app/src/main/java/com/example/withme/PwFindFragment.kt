package com.example.withme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.withme.databinding.FragmentPwFindBinding
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class PwFindFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind = FragmentPwFindBinding.inflate(layoutInflater)

        bind.pwFindButton.setOnClickListener {
            if(bind.idText.text.toString() == "" || bind.nameText.text.toString() == "" || bind.phoneText.text.toString() == "") {
                Toast.makeText(activity, "모든 정보를 입력해 주세요!", Toast.LENGTH_SHORT).show()
            } else {
                val url = ""
                val params = JSONObject()
                params.put("id", bind.idText.text.toString())
                params.put("name", bind.nameText.text.toString())
                params.put("phone", bind.phoneText.text.toString());

                val request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    params,
                    { response -> try {

                    } catch(error: JSONException) {
                        error.printStackTrace()
                    }
                    },
                    {
                            error -> {
                        error.printStackTrace()
                    }
                    }
                )

                Volley.newRequestQueue(activity).add(request)
            }
        }

        return bind.root
    }

}