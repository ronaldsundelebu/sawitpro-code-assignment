package android.template.core.data.model

data class TicketData(
    val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val licenseNumber: String = "",
    val driverName: String = "",
    val inWeight: Int = 0,
    val outWeight: Int = 0,
)
