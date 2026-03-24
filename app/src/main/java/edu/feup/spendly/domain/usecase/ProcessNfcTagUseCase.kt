package edu.feup.spendly.domain.usecase

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Parcelable
import edu.feup.spendly.domain.model.Expense
import javax.inject.Inject

/**
 * Use case to process scanned NFC tags and convert them to an Expense.
 * Requirement 3.6 Bonus: Read tags, Trigger actions.
 */
class ProcessNfcTagUseCase @Inject constructor() {
    
    /**
     * Parses the NFC intent and returns an Expense.
     * Expects text payload in format "amount|category|notes" e.g., "5.0|Food|Coffee"
     */
    operator fun invoke(intent: Intent): Expense? {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMsgs: Array<out Parcelable>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, Parcelable::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            }

            if (rawMsgs != null && rawMsgs.isNotEmpty()) {
                val msg = rawMsgs[0] as NdefMessage
                if (msg.records.isNotEmpty()) {
                    val payload = msg.records[0].payload
                    try {
                        val textEncoding = if ((payload[0].toInt() and 128) == 0) "UTF-8" else "UTF-16"
                        val languageCodeLength = payload[0].toInt() and 51
                        val text = String(
                            payload,
                            languageCodeLength + 1,
                            payload.size - languageCodeLength - 1,
                            charset(textEncoding)
                        )
                        
                        val parts = text.split("|")
                        val amount = parts.getOrNull(0)?.toDoubleOrNull() ?: 1.50
                        val category = parts.getOrNull(1) ?: "Other"
                        val notes = parts.getOrNull(2) ?: "Logged via NFC Tag"
                        
                        return Expense(
                            amount = amount,
                            category = category,
                            date = System.currentTimeMillis(),
                            notes = notes
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                
                // Fallback if parsing fails but tag is read
                return Expense(
                    amount = 1.50,
                    category = "Other",
                    date = System.currentTimeMillis(),
                    notes = "Logged via NFC Tag (Unknown Format)"
                )
            }
        }
        return null
    }
}
