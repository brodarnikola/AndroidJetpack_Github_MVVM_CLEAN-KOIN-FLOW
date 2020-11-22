/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.vjezba.androidjetpackgithub.ui.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.ui.mapper.ForecastViewState
import com.vjezba.androidjetpackgithub.ui.utilities.imageLoader.ImageLoader
import kotlinx.android.synthetic.main.list_item_flow_forecast.view.*

class ForecastFlowAdapter(
  private val layoutInflater: LayoutInflater,
  private val imageLoader: ImageLoader
) : RecyclerView.Adapter<ForecastFlowAdapter.ForecastViewHolder>() {

  private val forecasts: MutableList<ForecastViewState> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
    val itemView = layoutInflater.inflate(R.layout.list_item_flow_forecast, parent, false)
    return ForecastViewHolder(itemView, imageLoader)
  }

  override fun getItemCount() = forecasts.size

  override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) =
    holder.setItem(forecasts[position])

  fun setData(forecasts: List<ForecastViewState>) {
    this.forecasts.clear()
    this.forecasts.addAll(forecasts)
    notifyDataSetChanged()
  }

  class ForecastViewHolder(
    itemView: View,
    private val imageLoader: ImageLoader
  ) : RecyclerView.ViewHolder(itemView) {

    fun setItem(forecast: ForecastViewState) = with(itemView) {

      if( forecast.iconUrl == "" || forecast.iconUrl == null ) {
        Log.d(TAG, "da li ce sim uci: OOOO")
        imageLoader.load("aaaa", icon, context)
      }
      else
        imageLoader.load(forecast.iconUrl, icon, context)

      currentTemp.text = forecast.temp
      shortDescription.text = forecast.state
      date.text = forecast.date.toString()
      windSpeed.text = forecast.windSpeed
      airPressure.text = forecast.pressure
      humidity.text = forecast.humidity
      visibilityDistance.text = forecast.visibility
      predictability.text = forecast.predictability

      if( forecast.minMaxTemp.toString() == "" || forecast.minMaxTemp == null ) {
        Log.d(TAG, "da li ce sim uci: EEEE")
        minMaxTemp.text = "Currently unavailable value"
      }
      else
        minMaxTemp.text = forecast.minMaxTemp.toString()
    }
  }
}
