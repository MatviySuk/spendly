package edu.feup.spendly.presentation.add_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.feup.spendly.domain.model.Expense
import edu.feup.spendly.domain.usecase.AddExpenseUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Adding Expenses.
 * Requirement 3.2: Clear separation between UI and logic.
 * Requirement 3.6: Meaningful Sensor Integration (Location).
 */
@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {

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

    init {
        fetchCurrentLocation()
    }

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
     * If the user shakes the device, clear the form fields.
     */
//    fun onShakeDetected() {
//        _amount.value = ""
//        _category.value = "Food"
//        _notes.value = ""
//    }

    /**
     * Requirement 3.3 & 3.4: Save expense via Use Case.
     */
    fun saveExpense() {
        val amountValue = _amount.value.toDoubleOrNull() ?: return
        
        viewModelScope.launch {
            val newExpense = Expense(
                amount = amountValue,
                category = _category.value,
                date = System.currentTimeMillis(),
                location = _location.value,
                notes = _notes.value.ifBlank { null }
            )
            addExpenseUseCase(newExpense)
            _saveSuccess.emit(Unit)
        }
    }
}
