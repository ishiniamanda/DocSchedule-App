package com.nibm.docschedule.feature.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.nibm.docschedule.R

@Composable
fun IntroScreen(onStartClick: () -> Unit){
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = colorResource(R.color.purple))
    ){}
}