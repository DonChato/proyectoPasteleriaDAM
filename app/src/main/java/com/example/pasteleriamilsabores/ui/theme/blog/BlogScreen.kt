package com.example.pasteleriamilsabores.ui.blog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pasteleriamilsabores.R

data class Valor(val icono: String, val titulo: String, val descripcion: String)

val valoresList = listOf(
    Valor("❤️", "Pasión", "Amor por la repostería que se refleja en cada uno de nuestros productos."),
    Valor("🌱", "Tradición", "Respeto por las recetas ancestrales que han endulzado generaciones."),
    Valor("✨", "Calidad", "Utilizamos solo los mejores ingredientes en cada una de nuestras preparaciones."),
    Valor("🤝", "Comunidad", "Creemos en crecer junto a nuestra comunidad y apoyar a nuevos talentos.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nuestra Historia") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "Nuestra Trayectoria",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.historia_pasteleria),
                contentDescription = "Historia de Pastelería Mil Sabores",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Pastelería Mil Sabores nació en 1973 cuando la familia González decidió compartir sus recetas tradicionales con la comunidad. Lo que comenzó como un pequeño local familiar en el barrio Yungay de Santiago, pronto se convirtió en un referente de la repostería chilena.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Hoy, después de 50 años, mantenemos vivo el legado de nuestras recetas originales mientras incorporamos nuevas técnicas y sabores, siempre con el mismo amor y dedicación que nos caracteriza.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            Text(
                "Misión y Visión",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Misión",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Ofrecer una experiencia dulce y memorable a nuestros clientes, proporcionando productos de alta calidad para todas las ocasiones.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Visión",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Convertirnos en la tienda online líder de repostería en Chile, conocida por nuestra innovación y calidad.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "Nuestros Valores",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            valoresList.forEach { valor ->
                ValorCard(valor = valor)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ValorCard(valor: Valor) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = valor.icono, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = valor.titulo,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = valor.descripcion,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
