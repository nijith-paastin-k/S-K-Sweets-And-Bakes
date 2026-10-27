package com.skylink.sksweetsandbakes.presentation.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.skylink.sksweetsandbakes.core.designsystem.SKSweetsTopBar
import com.skylink.sksweetsandbakes.core.designsystem.StatusBadge
import com.skylink.sksweetsandbakes.ui.theme.AppDimens

data class StaffMember(
    val id: String,
    val name: String,
    val phone: String,
    val role: String,
    val permissions: String
)

@Composable
fun StaffManagementScreen(
    onBackClick: () -> Unit
) {
    val staffList = listOf(
        StaffMember(
            "stf_01",
            "Anand S.",
            "+91 98801 12345",
            "CASHIER",
            "Can access POS, cannot view profits/expenses"
        ),
        StaffMember(
            "stf_02",
            "Praveen K.",
            "+91 97412 34567",
            "MANAGER",
            "Can manage orders, pricing & stock, cannot change roles"
        ),
        StaffMember(
            "stf_03",
            "Murugan M.",
            "+91 94483 56789",
            "INVENTORY_CLERK",
            "Can log batch receiving and wastage"
        )
    )

    Scaffold(
        topBar = {
            SKSweetsTopBar(
                title = "Staff Accounts & Permissions",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(AppDimens.dp16),
            verticalArrangement = Arrangement.spacedBy(AppDimens.dp12)
        ) {
            items(staffList) { staff ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(AppDimens.dp14),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(AppDimens.dp16)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = staff.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            StatusBadge(status = staff.role)
                        }
                        Spacer(modifier = Modifier.height(AppDimens.dp4))
                        Text(
                            text = "Phone: ${staff.phone}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(AppDimens.dp6))
                        Text(
                            text = "Permission Gate: ${staff.permissions}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
