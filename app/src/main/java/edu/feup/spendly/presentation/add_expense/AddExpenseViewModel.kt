package edu.feup.spendly.presentation.add_expense

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.usecase.AddExpenseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import kotlin.math.sqrt

/**
 * ViewModel for Adding Expenses.
 * Requirement 3.2: Clear separation between UI and logic.
 * Requirement 3.6: Meaningful Sensor Integration (Location & Accelerometer).
 */
@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(), SensorEventListener {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Form State
    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _category = MutableStateFlow("Food")
    val category: StateFlow<String> = _category

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes

    private val _location = MutableStateFlow<String?>(null)
    val location: StateFlow<String?> = _location

    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess.asSharedFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }

    // --- Sensor Callbacks ---

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
            if (acceleration > 12) { // Shake threshold
                clearForm()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun clearForm() {
        _amount.value = ""
        _notes.value = ""
        _error.value = "Form cleared by shake!"
    }

    // --- Form Actions ---

    fun onAmountChange(newAmount: String) {
        if (newAmount.all { it.isDigit() || it == '.' }) {
            _amount.value = newAmount
        }
    }

    fun onCategoryChange(newCategory: String) {
        _category.value = newCategory
    }

    fun onNotesChange(newNotes: String) {
        _notes.value = newNotes
    }

    fun onLocationManualChange(newLocation: String) {
        _location.value = newLocation.ifBlank { null }
    }

    /**
     * Requirement 3.6: Integrate Android Location Sensor.
     */
    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                val locationResult: Location? = fusedLocationClient.lastLocation.await()
                if (locationResult != null) {
                    val address = getAddressFromLocation(locationResult.latitude, locationResult.longitude)
                    _location.value = address ?: "Lat: ${locationResult.latitude}, Lng: ${locationResult.longitude}"
                }
            } catch (e: Exception) {
                _location.value = null
            }
        }
    }

    private suspend fun getAddressFromLocation(lat: Double, lng: Double): String? = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                return@withContext address.locality ?: address.subAdminArea ?: address.adminArea ?: address.countryName
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    /**
     * Requirement 3.3 & 3.4: Save expense via Use Case.
     */
    fun saveExpense() {
        val amountValue = _amount.value.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _error.value = "Please enter a valid amount"
            return
        }
        
        viewModelScope.launch {
            try {
                val newExpense = Expense(
                    amount = amountValue,
                    category = _category.value,
                    date = System.currentTimeMillis(),
                    location = _location.value,
                    notes = _notes.value.ifBlank { null }
                )
                addExpenseUseCase(newExpense)
                _saveSuccess.emit(Unit)
            } catch (e: Exception) {
                _error.value = "Failed to save expense locally"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
