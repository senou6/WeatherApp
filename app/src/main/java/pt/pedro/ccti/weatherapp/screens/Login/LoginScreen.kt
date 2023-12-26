package pt.pedro.ccti.weatherapp.screens.loginscreen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pt.pedro.ccti.weatherapp.components.FrontImage
import pt.pedro.ccti.weatherapp.components.InputField
import pt.pedro.ccti.weatherapp.components.Loading
import pt.pedro.ccti.weatherapp.components.PasswordInput
import pt.pedro.ccti.weatherapp.navigation.WeatherScreens

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }




    if (loginViewModel.isLoading) {
        Loading()
    } else {

        Column(
            modifier= Modifier
                .padding(16.dp)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FrontImage()

            errorMessage?.value?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            InputField(valueState = email, labelId = "Email")
            PasswordInput(
                passwordState = password,
                labelId = "Password",
                passwordVisibility = passwordVisibility
            )

            Button(onClick = {
                loginViewModel.loginUser(email.value, password.value, {
                    navController.navigate(WeatherScreens.HomeScreen.name) {
                        popUpTo("unauthenticated_graph") {
                            inclusive = true
                        }
                    }
                })
                { error -> errorMessage.value = error }},
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Text("Login")
            }

            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Don't have an account?", color = Color.White)
                Text("Sign Up Here",
                    modifier = Modifier
                        .clickable{
                            navController.navigate(WeatherScreens.RegisterScreen.name)
                        }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = Color.White)

            }
        }
    }
}
