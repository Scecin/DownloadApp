package com.udacity

// Button state with the text in any state
sealed class ButtonState(var buttonState : Int) {
    object Clicked : ButtonState(R.string.download)
    object Loading : ButtonState(R.string.loading)
    object Completed : ButtonState(R.string.complete)
}