package com.saddam.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.saddam.storyapp.R

class PasswordEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var hideIcon: Drawable

    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = ContextCompat.getDrawable(context, R.drawable.bg_edit_text)
    }

    private fun init() {
        hideIcon = ContextCompat.getDrawable(context, R.drawable.ic_remove_red_eye_24) as Drawable
        setOnTouchListener(this)

        showIcon()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun showIcon() {
        setButtonDrawables(endOfTheText = hideIcon)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(p0: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val hidePasswordButtonStart: Float
            val hidePasswordButtonEnd: Float
            var isHidePasswordButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                hidePasswordButtonEnd = (hideIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < hidePasswordButtonEnd -> isHidePasswordButtonClicked = true
                }
            } else {
                hidePasswordButtonStart = (width - paddingEnd - hideIcon.intrinsicWidth).toFloat()
                when {
                    event.x > hidePasswordButtonStart -> isHidePasswordButtonClicked = true
                }
            }
            if (isHidePasswordButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_UP -> {
                       if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD){
                           inputType = InputType.TYPE_CLASS_TEXT
                           transformationMethod = PasswordTransformationMethod.getInstance()
                       }else{
                           inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                           transformationMethod = HideReturnsTransformationMethod.getInstance()
                       }
                        setSelection(text?.length!!)
                        true
                    }
                    else -> false
                }
            }
        }
        return false
    }
}