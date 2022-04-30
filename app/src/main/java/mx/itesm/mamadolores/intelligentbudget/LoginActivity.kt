package mx.itesm.mamadolores.intelligentbudget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import mx.itesm.mamadolores.intelligentbudget.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Registrar evento
        binding.btnLogIn.setOnClickListener{
            autenticar()
        }

        verificarLogin()
    }

    private fun verificarLogin() {
        val usuario = FirebaseAuth.getInstance().currentUser
        if (usuario != null){
            //Ya está logueado
            println("Bienvenido! *** ${usuario.displayName} ***")
            entrarApp()
        }
    }

    private fun entrarApp() {
        //Entrar a la app
        val intApp = Intent(this, MainActivity::class.java)
        startActivity(intApp)

        //Borrar pantalla
        finish() // Descarga de memoria de la actividad.
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()){
            res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK){
            val usuario = FirebaseAuth.getInstance().currentUser
            println("Bienvenido: ${usuario?.displayName}")
            println("Correo: ${usuario?.email}")
            println("Token: ${usuario?.uid}")

            entrarApp()

        } else {
            println("Credenciales incorrectas")
        }
    }

    private fun autenticar() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }


}