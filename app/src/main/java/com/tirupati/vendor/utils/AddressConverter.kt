package com.tirupati.vendor.utils

import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AddressConverter(private val apiKey: String) {

    fun getAddressFromLatLng(latitude: Double, longitude: Double, callback: (String?) -> Unit) {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$latitude,$longitude&key=$apiKey"
        FetchAddressTask(callback).execute(url)
    }

    private inner class FetchAddressTask(private val callback: (String?) -> Unit) :
        AsyncTask<String, Void, String?>() {

        override fun doInBackground(vararg urls: String): String? {
            return try {
                val url = URL(urls[0])
                val connection = url.openConnection() as HttpURLConnection
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                reader.close()
                connection.disconnect()
                stringBuilder.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val formattedAddress = parseAddress(result)
            callback(formattedAddress)
        }

        private fun parseAddress(jsonResult: String?): String? {
            return try {
                val jsonObject = JSONObject(jsonResult)
                if (jsonObject.getString("status") == "OK") {
                    val results = jsonObject.getJSONArray("results")
                    if (results.length() > 0) {
                        results.getJSONObject(0).getString("formatted_address")
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                null
            }
        }
    }
}
