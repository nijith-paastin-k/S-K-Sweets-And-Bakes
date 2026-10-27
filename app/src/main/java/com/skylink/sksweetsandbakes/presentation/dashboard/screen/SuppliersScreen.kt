package com.skylink.sksweetsandbakes.presentation.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.data.seed.SeedData
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@Composable
fun SuppliersScreen(
    onBackClick: () -> Unit
) {
    val suppliers = SeedData.suppliers

    Scaffold(
        topBar = { SKSweetsTopBar(title = "Suppliers & Procurement", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
        ) {
            items(suppliers) { sup ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = sup.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                        Text(
                            text = "Contact: ${sup.contactPerson} • ${sup.phoneNumber}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Email: ${sup.email}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Address: ${sup.address}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                        Text(
                            text = "GSTIN: ${sup.gstin}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
