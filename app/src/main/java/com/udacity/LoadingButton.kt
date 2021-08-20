package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.nfc.Tag
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * Variable to draw the custom view
     */
    private var widthSize = 0
    private var heightSize = 0
    private var progress = 0f
    private var angle = 0f
    private var loadingWidth = 0f
    private var buttonText = ""

    /**
     * Painting and Styling
     */
    // The button
    private val paintButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorPrimary)
    }

    // The button during the process to download
    private val paintProgressButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorPrimaryDark)
    }

    // The text on the button
    private val paintButtonText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.white)
        textSize = 50f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    // The circle
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorAccent)
    }

    private var valueAnimator = ValueAnimator()

    var value = 0.0f
    var width = 0.0f
    var sweepAngle = 0.0f

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        // Set the button text depending on the state of the button
        buttonText = context.getString(buttonState.buttonState)

        when(new) {

            ButtonState.Loading -> {
                paintCircle.color = context.getColor(R.color.colorAccent)
                valueAnimator = ValueAnimator.ofFloat(0.0f,
                    measuredWidth.toFloat())
                    .setDuration(2000)
                    .apply {
                        addUpdateListener { valueAnimator ->
                            value = valueAnimator.animatedValue as Float
                            sweepAngle = value / 8
                            width = valueAnimator.animatedValue as Float
                            repeatMode = ValueAnimator.RESTART
                            repeatCount = 1
                            invalidate()
                        }
                    }
                // disable during animation
//                valueAnimator.disableDuringAnimation(custom_button)
                isEnabled = false
                valueAnimator.start()
            }

            ButtonState.Completed -> {
                // Reset to default state
                width = 0f
                sweepAngle = 0f
                valueAnimator.cancel()
                value = 0.0f
                invalidate()
                isEnabled = true
            }
        }
    }


    init {
        buttonState = ButtonState.Clicked
    }


    /**
     * On draw method to draw the custom view
     * */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0.0f, 0.0f, widthSize.toFloat(), heightSize.toFloat(), paintButton)

        canvas?.drawRect(0f, 0f, width, heightSize.toFloat(), paintProgressButton)

        val textHeight: Float = paintButtonText.descent() - paintButtonText.ascent()
        val textOffset: Float = textHeight / 2 - paintButtonText.descent()
        canvas?.drawText(buttonText, widthSize.toFloat() / 2.0f, heightSize.toFloat() / 2.0f + 18, paintButtonText)

        canvas?.drawArc(widthSize - 145f,
            heightSize / 2 - 35f,
            widthSize - 75f,
            heightSize / 2 + 35f,
            0F,
            width,
            true,
            paintCircle)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


//    private fun ValueAnimator.disableDuringAnimation(view: View) {
//        addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationStart(animation: Animator?) {
//                view.isEnabled = false
//            }
//            override fun onAnimationEnd(animation: Animator?) {
//                view.isEnabled = true
//            }
//        })
//    }

}