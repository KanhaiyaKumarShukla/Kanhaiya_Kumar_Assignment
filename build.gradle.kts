// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// Enforce consistent versions across the build to avoid runtime/processing mismatches
allprojects {
    configurations.all {
        resolutionStrategy {
            // Prevent NoSuchMethodError: ClassName.canonicalName() by forcing JavaPoet version
            force("com.squareup:javapoet:1.13.0")
        }
    }
}