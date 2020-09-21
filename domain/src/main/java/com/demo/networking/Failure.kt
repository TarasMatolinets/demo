package com.demo.networking

import com.demo.networking.Failure.FeatureFailure

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    var errorMessage: String? = null

    operator fun invoke(e: Exception) {
        errorMessage = e.message
    }

    object NetworkConnection : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}
