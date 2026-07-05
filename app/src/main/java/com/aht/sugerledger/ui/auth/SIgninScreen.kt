package com.aht.sugerledger.ui.auth


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aht.sugerledger.R
import com.aht.sugerledger.ui.theme.BrandOrange
import com.aht.sugerledger.ui.theme.IconColor
import com.aht.sugerledger.ui.theme.SugerLedgerTheme

@Composable
fun SignInScreen(
    onSignupClick : () -> Unit,
    onSigninClick : () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var selectedTab by rememberSaveable { mutableStateOf(0) } // 0 = Login, 1 = Register
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    SignInContent(
        email = email,
        password = password,
        passwordVisibility = passwordVisible,
        selectedTab = selectedTab,
        rememberMe = rememberMe,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
        onTabSelected = { selectedTab = it },
        onRememberMeChange = { rememberMe = it },
        onSignUpClick = onSignupClick,
        onSignInClick = onSigninClick,
        modifier = Modifier.fillMaxSize()
    )

}

@Composable
fun SignInContent (
    email : String,
    password : String,
    passwordVisibility : Boolean,
    selectedTab : Int  = 0,
    rememberMe : Boolean = false,
    onEmailChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    onPasswordVisibilityChange : () -> Unit,
    onTabSelected : (Int) -> Unit,
    onRememberMeChange  : (Boolean) -> Unit,
    onSignUpClick : () -> Unit,
    onSignInClick : () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .background(color = Color.White)
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // بخش تب‌سوئیچر پاتیلی شکل
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 32.dp)
                .height(46.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(50.dp)
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            // تب لاگین
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (selectedTab == 0) BrandOrange else Color.Transparent,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .clickable { onTabSelected(0) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (selectedTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Login",
                        color = if (selectedTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // تب رجیستر
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (selectedTab == 1) BrandOrange else Color.Transparent,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .clickable { onTabSelected(1) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (selectedTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Register",
                        color = if (selectedTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // بخش آیکون و هدر اپلیکیشن
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "Sugar Ledger Icon",
        )
        Text(
            text = "Sugar Ledger",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Confectionary ERP & Accounting App",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // بخش فیلدهای ورودی متناسب با پدینگ استاندارد دکمه‌ها
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                // استفاده از آیکون‌های استاندارد چشم برای دیدن پسورد
                val image = if (passwordVisibility) Icons.Default.Lock else Icons.Default.Info
                IconButton(onClick = onPasswordVisibilityChange) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        // بخش چک‌باکس و رمز عبور فراموش شده (با انیمیشن تغییر وضعیت تب)
        if (selectedTab == 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = onRememberMeChange,
                    )
                    Text("Remember me", fontSize = 12.sp)
                }
                TextButton(onClick = { /* Handle Forget Password */ }) {
                    Text("Forget Password?", fontSize = 12.sp, color = BrandOrange)
                }
            }
        } else {
            // ایجاد یک پدینگ تعادلی برای زمانی که تب رجیستر باز است تا صفحه پرش نکند
            Spacer(modifier = Modifier.height(48.dp))
        }

        // دکمه اصلی عملیات (ورود / ثبت‌نام) تغییر نام پویا بر اساس تب انتخاب شده
        OutlinedButton(
            onClick = { if (selectedTab == 0) onSignInClick() else onSignUpClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = BrandOrange
            )
        ) {
            Text(
                text = if (selectedTab == 0) "Sign in" else "Sign up",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // خط جداکننده OR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "or",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        // دکمه گوگل کاملاً شکیل و ریپوزیشنی
        OutlinedButton(
            onClick = { /* Handle Google Sign In */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color(0xFFDDDDDD))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Sign in with Google",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // بخش فوتر امنیتی دیتا
        Row(
            modifier = Modifier.padding(horizontal = 32.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Secure",
                tint = Color(0xFFE5961F),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Your data is secure with us. We follow best practices to protect your information.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Preview("default")
@Composable
private fun SignInPreview () {
    SugerLedgerTheme {
        SignInContent(
            "amirhossin@gamilc.con",
            "amir1234",
            false,
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onTabSelected = {},
            onRememberMeChange = {},
            onSignInClick = {},
            onSignUpClick = {},
            modifier = Modifier
        )
    }
}