package edu.feup.spendly.domain.usecase

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import edu.feup.spendly.domain.model.Expense
import javax.inject.Inject

/**
 * Use case to process scanned NFC tags and convert them to an Expense.
 * Requirement 3.6 Bonus: Read tags, Trigger actions.
 */
class ProcessNfcTagUseCase @Inject constructor() {
    
    /**
     * Parses the NFC intent and returns a predefined Expense.
     * TODO: Implement actual NDEF message parsing.
     * Example: A tag could contain "5.0|Coffee|Food"
     */
    operator fun invoke(intent: Intent): Expense? {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMsgs != null) {
                // val msg = rawMsgs[0] as NdefMessage
                // TODO: Parse msg.records[0].payload into an Expense object.
                
                // Placeholder for a "Quick Log" expense
                return Expense(
                    amount = 1.50,
                    category = "NFC Quick Log",
                    date = System.currentTimeMillis(),
                    notes = "Logged via NFC Tag"
                )
            }
        }
        return null
    }
}
