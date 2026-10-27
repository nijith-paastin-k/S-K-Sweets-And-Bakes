package com.skylink.sksweetsandbakes.presentation.dashboard.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

@Composable
fun BusinessSettingsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { SKSweetsTopBar(title = "Business Configuration", onBackClick = onBackClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(AppDimens.dp16)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Text(
                            text = "Store Details",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp8))
                        Text(
                            text = "Name: S K Sweets & Bakes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Outlet Address: #42, Main Market Road, Bengaluru, Karnataka 560001",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Store Status: Open Daily • Fresh Batches (All 7 Days)",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Delivery Radius: 12 Kilometres",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Default Sweet GST: 5%",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Free Delivery Threshold: ₹1,000",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
