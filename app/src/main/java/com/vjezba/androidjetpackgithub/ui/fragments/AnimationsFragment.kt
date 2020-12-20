/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.androidjetpackgithub.ui.fragments

import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.*
import android.view.animation.Animation.AnimationListener
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentAnimationsBinding
import com.vjezba.androidjetpackgithub.ui.utilities.returnToPreviousImage
import com.vjezba.androidjetpackgithub.ui.utilities.scaleDown
import com.vjezba.androidjetpackgithub.ui.utilities.screenWidth
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_animations.*


class AnimationsFragment : Fragment() {


    private var set: AnimationSet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAnimationsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.speedDial?.visibility = View.GONE

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        val screenWidth = screenWidth(this.requireActivity())

        loading_logo.setImageBitmap(
            scaleDown(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.splash_logo
                ), (screenWidth * 0.6).toFloat(), false
            )
        )

//        val width: Float = ivBg.getDrawable().getIntrinsicWidth().toString().toFloat()
//        val finalWidth = width - returnToPreviousImage(this.requireActivity())
//        val animationBackgroundImage = ObjectAnimator.ofFloat(ivBg, "translationX", finalWidth )
//        animationBackgroundImage.duration = 3000
//        animationBackgroundImage.start()
//        animationBackgroundImage.doOnEnd {
//
//            val returnToPreviousImage50Percent = returnToPreviousImage(this.requireActivity())
//            val animationReturnToPreviousBackgroundImage = ObjectAnimator.ofFloat(ivBg, "translationX", -returnToPreviousImage50Percent)
//            animationReturnToPreviousBackgroundImage.duration = 3000
//            animationReturnToPreviousBackgroundImage.start()
//        }

        val vto: ViewTreeObserver = ivBg.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //get Image Width and height here
                val imageWidth = ivBg.width.toFloat()
                val imageHeight: Int = ivBg.getHeight()

                val animationBackgroundImage = ObjectAnimator.ofFloat(ivBg, "translationX", (-imageWidth+ screenWidth) )
                animationBackgroundImage.duration = 3000
                animationBackgroundImage.start()
                animationBackgroundImage.doOnEnd {

                    val animationReturnToPreviousBackgroundImage = ObjectAnimator.ofFloat(ivBg, "translationX", 0f)
                    animationReturnToPreviousBackgroundImage.duration = 3000
                    animationReturnToPreviousBackgroundImage.start()
                }

                //Then remove layoutChange Listener
                val vto: ViewTreeObserver = ivBg.getViewTreeObserver()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    vto.removeOnGlobalLayoutListener(this)
                } else {
                    vto.removeGlobalOnLayoutListener(this)
                }
            }
        })







        set = AnimationSet(true)

        val alphaAnimation = AlphaAnimation(0.2f, 1.0f)

        val scaleAnimation: Animation = ScaleAnimation(
            0.3f,
            1f,
            0.3f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        val rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.interpolator = LinearInterpolator()

        set?.addAnimation(alphaAnimation)
        set?.addAnimation(scaleAnimation)
        set?.addAnimation(rotateAnimation)
        set?.setDuration(3000)

        // Now set animatio  listener on your rotate animation
        set?.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                var topMargin = 0.0f // top margin is -25% of screen

                topMargin = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
                    val display: Display =  this@AnimationsFragment.requireActivity().windowManager.defaultDisplay
                    val width = display.width
                    val height = display.height
                    (height * 0.25).toFloat() // top margin is -25% of screen
                } else {
                    val display: Display = this@AnimationsFragment.requireActivity().windowManager.defaultDisplay
                    val size = Point()
                    display.getSize(size)
                    val width = size.x
                    val height = size.y
                    (height * 0.25).toFloat() // top margin is -25% of screen
                }

                loading_logo.animate().translationX(0f).translationY(-topMargin).setDuration(1000).start();
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        loading_logo.startAnimation(set)

    }

}
