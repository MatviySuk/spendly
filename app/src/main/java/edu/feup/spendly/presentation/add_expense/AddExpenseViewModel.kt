package edu.feup.spendly.presentation.add_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.usecase.AddExpenseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Adding Expenses.
 * Requirement 3.6: Meaningful Sensor Integration (Location).
 */
@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {

    private val _location = MutableStateFlow<String?>(null)
    val location: StateFlow<String?> = _location

    init {
        fetchCurrentLocation()
    }

    /**
     * Requirement 3.6: Integrate Android Location Sensor.
     * TODO: 
     * 1. Check for permission (Manifest is already updated).
     * 2. Use FusedLocationProviderClient to get coordinates.
     * 3. Use Geocoder to update _location.value.
     */
    fun fetchCurrentLocation() {
        // Placeholder for implementation
    }

    /**
     * Requirement 3.6 Bonus: Accelerometer Sensor.
     * TODO: Implement "Shake to Clear".
     * If the user shakes the device, clear the form fields.
     */
    fun onShakeDetected() {
        // Logic to clear fields
    }

    fun saveExpense(amount: Double, category: String, notes: String?) {
        viewModelScope.launch {
            val newExpense = Expense(
                amount = amount,
                category = category,
                date = System.currentTimeMillis(),
                location = _location.value,
                notes = notes
            )
            addExpenseUseCase(newExpense)
        }
    }
}
