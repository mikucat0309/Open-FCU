package at.mikuc.openfcu.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val M2DarkColors = darkColors(
    primary = M3.Dark.primary,
    onPrimary = M3.Dark.onPrimary,
//    primaryContainer = M3.Dark.primaryContainer,
//    onPrimaryContainer = M3.Dark.onPrimaryContainer,
    secondary = M3.Dark.secondary,
    onSecondary = M3.Dark.onSecondary,
//    secondaryContainer = M3.Dark.secondaryContainer,
//    onSecondaryContainer = M3.Dark.onSecondaryContainer,
//    tertiary = M3.Dark.tertiary,
//    onTertiary = M3.Dark.onTertiary,
//    tertiaryContainer = M3.Dark.tertiaryContainer,
//    onTertiaryContainer = M3.Dark.onTertiaryContainer,
    error = M3.Dark.error,
//    errorContainer = M3.Dark.errorContainer,
    onError = M3.Dark.onError,
//    onErrorContainer = M3.Dark.onErrorContainer,
    background = M3.Dark.background,
    onBackground = M3.Dark.onBackground,
    surface = M3.Dark.surface,
    onSurface = M3.Dark.onSurface,
//    surfaceVariant = M3.Dark.surfaceVariant,
//    onSurfaceVariant = M3.Dark.onSurfaceVariant,
//    outline = M3.Dark.outline,
//    inverseOnSurface = M3.Dark.inverseOnSurface,
//    inverseSurface = M3.Dark.inverseSurface,
//    inversePrimary = M3.Dark.inversePrimary,
//    surfaceTint = M3.Dark.surfaceTint,
//    surfaceTintColor = M3.Dark.surfaceTintColor,
)

private val M2LightColors = lightColors(
    primary = M3.Light.primary,
    onPrimary = M3.Light.onPrimary,
//    primaryContainer = M3.Light.primaryContainer,
//    onPrimaryContainer = M3.Light.onPrimaryContainer,
    secondary = M3.Light.secondary,
    onSecondary = M3.Light.onSecondary,
//    secondaryContainer = M3.Light.secondaryContainer,
//    onSecondaryContainer = M3.Light.onSecondaryContainer,
//    tertiary = M3.Light.tertiary,
//    onTertiary = M3.Light.onTertiary,
//    tertiaryContainer = M3.Light.tertiaryContainer,
//    onTertiaryContainer = M3.Light.onTertiaryContainer,
    error = M3.Light.error,
//    errorContainer = M3.Light.errorContainer,
    onError = M3.Light.onError,
//    onErrorContainer = M3.Light.onErrorContainer,
    background = M3.Light.background,
    onBackground = M3.Light.onBackground,
    surface = M3.Light.surface,
    onSurface = M3.Light.onSurface,
//    surfaceVariant = M3.Light.surfaceVariant,
//    onSurfaceVariant = M3.Light.onSurfaceVariant,
//    outline = M3.Light.outline,
//    inverseOnSurface = M3.Light.inverseOnSurface,
//    inverseSurface = M3.Light.inverseSurface,
//    inversePrimary = M3.Light.inversePrimary,
//    surfaceTint = M3.Light.surfaceTint,
//    surfaceTintColor = M3.Light.surfaceTintColor,
)

typealias MaterialTheme3 = androidx.compose.material3.MaterialTheme

private val M3LightColors = lightColorScheme(
    primary = M3.Light.primary,
    onPrimary = M3.Light.onPrimary,
    primaryContainer = M3.Light.primaryContainer,
    onPrimaryContainer = M3.Light.onPrimaryContainer,
    secondary = M3.Light.secondary,
    onSecondary = M3.Light.onSecondary,
    secondaryContainer = M3.Light.secondaryContainer,
    onSecondaryContainer = M3.Light.onSecondaryContainer,
    tertiary = M3.Light.tertiary,
    onTertiary = M3.Light.onTertiary,
    tertiaryContainer = M3.Light.tertiaryContainer,
    onTertiaryContainer = M3.Light.onTertiaryContainer,
    error = M3.Light.error,
    errorContainer = M3.Light.errorContainer,
    onError = M3.Light.onError,
    onErrorContainer = M3.Light.onErrorContainer,
    background = M3.Light.background,
    onBackground = M3.Light.onBackground,
    surface = M3.Light.surface,
    onSurface = M3.Light.onSurface,
    surfaceVariant = M3.Light.surfaceVariant,
    onSurfaceVariant = M3.Light.onSurfaceVariant,
    outline = M3.Light.outline,
    inverseOnSurface = M3.Light.inverseOnSurface,
    inverseSurface = M3.Light.inverseSurface,
    inversePrimary = M3.Light.inversePrimary,
    surfaceTint = M3.Light.surfaceTint,
)

private val M3DarkColors = darkColorScheme(
    primary = M3.Dark.primary,
    onPrimary = M3.Dark.onPrimary,
    primaryContainer = M3.Dark.primaryContainer,
    onPrimaryContainer = M3.Dark.onPrimaryContainer,
    secondary = M3.Dark.secondary,
    onSecondary = M3.Dark.onSecondary,
    secondaryContainer = M3.Dark.secondaryContainer,
    onSecondaryContainer = M3.Dark.onSecondaryContainer,
    tertiary = M3.Dark.tertiary,
    onTertiary = M3.Dark.onTertiary,
    tertiaryContainer = M3.Dark.tertiaryContainer,
    onTertiaryContainer = M3.Dark.onTertiaryContainer,
    error = M3.Dark.error,
    errorContainer = M3.Dark.errorContainer,
    onError = M3.Dark.onError,
    onErrorContainer = M3.Dark.onErrorContainer,
    background = M3.Dark.background,
    onBackground = M3.Dark.onBackground,
    surface = M3.Dark.surface,
    onSurface = M3.Dark.onSurface,
    surfaceVariant = M3.Dark.surfaceVariant,
    onSurfaceVariant = M3.Dark.onSurfaceVariant,
    outline = M3.Dark.outline,
    inverseOnSurface = M3.Dark.inverseOnSurface,
    inverseSurface = M3.Dark.inverseSurface,
    inversePrimary = M3.Dark.inversePrimary,
    surfaceTint = M3.Dark.surfaceTint,
)

@Composable
fun MixMaterialTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) M2DarkColors else M2LightColors,
        typography = Typography,
        shapes = Shapes,
        content = {
            androidx.compose.material3.MaterialTheme(
                colorScheme = if (darkTheme)  M3DarkColors else M3LightColors,
                typography = AppTypography,
                content = content
            )
        }
    )
}
