package edu.feup.spendly

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Spendly.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation.
 */
@HiltAndroidApp
class SpendlyApp : Application()
