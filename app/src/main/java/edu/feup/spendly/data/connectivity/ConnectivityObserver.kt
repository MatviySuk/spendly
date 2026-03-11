package edu.feup.spendly.data.connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Interface for monitoring network status.
 * Requirement 3.7: Offline-First strategy / Network Awareness.
 */
interface ConnectivityObserver {
    
    enum class Status {
        Available, Unavailable, Losing, Lost
    }

    fun observe(): Flow<Status>
}
