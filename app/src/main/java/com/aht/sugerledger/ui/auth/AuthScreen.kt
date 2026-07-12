package com.aht.sugerledger.ui.auth

import androidx.compose.runtime.Composable

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit
) {
    SignInScreen(
        onSignupClick = onAuthenticated,
        onSigninClick = onAuthenticated
    )
}
