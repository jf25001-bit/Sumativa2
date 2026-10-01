package com.example.sumativa2sej.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumativa2sej.components.CustomOutlinedTextField

@Composable
fun CalculatorView() {
    val context = LocalContext.current

    var nombrePlatillo by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var precioUnitario by remember { mutableStateOf("") }
    var porcentajePropina by remember { mutableStateOf("") }

    var propinaTotal by remember { mutableDoubleStateOf(0.0) }
    var totalPagar by remember { mutableDoubleStateOf(0.0) }

    var errorNombre by remember { mutableStateOf(false) }
    var errorCantidad by remember { mutableStateOf(false) }
    var errorPrecio by remember { mutableStateOf(false) }
    var errorPropina by remember { mutableStateOf(false) }
    var mensajeErrorPropina by remember { mutableStateOf<String?>(null) }

    fun calcular() {
        errorNombre = nombrePlatillo.isBlank()
        errorCantidad = cantidad.isBlank()
        errorPrecio = precioUnitario.isBlank()
        errorPropina = porcentajePropina.isBlank()

        if (errorPropina) {
            mensajeErrorPropina = "Campo requerido"
        }

        if (errorNombre || errorCantidad || errorPrecio || errorPropina) {
            Toast.makeText(context, "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val cant = cantidad.toIntOrNull() ?: 0
        val precio = precioUnitario.toDoubleOrNull() ?: 0.0
        val porcentaje = porcentajePropina.toDoubleOrNull() ?: 0.0

        if (porcentaje > 10.0) {
            errorPropina = true
            mensajeErrorPropina = "El porcentaje no debe exceder el 10%"
            Toast.makeText(context, "La propina no debe exceder el 10%", Toast.LENGTH_SHORT).show()
            return
        } else {
            errorPropina = false
            mensajeErrorPropina = null
        }

        val subtotal = cant * precio
        val propinaCalculada = subtotal * (porcentaje / 100.0)

        propinaTotal = propinaCalculada
        totalPagar = subtotal + propinaCalculada
    }

    fun limpiar() {
        nombrePlatillo = ""
        cantidad = ""
        precioUnitario = ""
        porcentajePropina = ""
        propinaTotal = 0.0
        totalPagar = 0.0
        errorNombre = false
        errorCantidad = false
        errorPrecio = false
        errorPropina = false
        mensajeErrorPropina = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Propina", fontSize = 14.sp)
                Text(text = "$${String.format("%.2f", propinaTotal)}", fontSize = 28.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Total a pagar", fontSize = 14.sp)
                Text(text = "$${String.format("%.2f", totalPagar)}", fontSize = 28.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomOutlinedTextField(
            value = nombrePlatillo,
            onValueChange = { nombrePlatillo = it; errorNombre = false },
            label = "Nombre del platillo",
            isError = errorNombre,
            errorMessage = if (errorNombre) "Campo requerido" else null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomOutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it; errorCantidad = false },
            label = "Cantidad",
            isNumberKeyboard = true,
            isError = errorCantidad,
            errorMessage = if (errorCantidad) "Campo requerido" else null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomOutlinedTextField(
            value = precioUnitario,
            onValueChange = { precioUnitario = it; errorPrecio = false },
            label = "Precio unitario",
            isNumberKeyboard = true,
            isError = errorPrecio,
            errorMessage = if (errorPrecio) "Campo requerido" else null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomOutlinedTextField(
            value = porcentajePropina,
            onValueChange = { porcentajePropina = it; errorPropina = false },
            label = "Porcentaje de propina (máx. 10%)",
            isNumberKeyboard = true,
            isError = errorPropina,
            errorMessage = mensajeErrorPropina,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { calcular() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Calcular Total", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { limpiar() }) {
            Text(text = "Limpiar", fontSize = 16.sp)
        }
    }
}