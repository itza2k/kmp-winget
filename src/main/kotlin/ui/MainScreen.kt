package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ModeNight
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.WbSunny
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.PerformAction
import theme.AppTheme
import theme.ThemeState
import utils.bodyFont
import utils.headingFont
import utils.performAction

@Composable
@Preview
fun MainScreen() {
    var packages by remember { mutableStateOf<List<model.Package>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    val isDarkMode = ThemeState.isDarkMode.value

    performAction(
        scope = scope,
        onPackagesLoaded = { result ->
            packages = result
        },
        setLoading = { isLoading = it },
        action = PerformAction.RefreshList
    )

    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.surface)
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Package Manager",
                    fontFamily = headingFont,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 21.sp,
                )

                Spacer(modifier = Modifier.width(10.dp))

                DynamicIconButton(
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier.size(36.dp),
                    onClickAction = {
                        errorMessage = null
                        performAction(
                            scope = scope,
                            onPackagesLoaded = { result ->
                                packages = result
                            },
                            setLoading = { isLoading = it },
                            action = PerformAction.RefreshList
                        )
                    },
                    isEnabled = !isLoading,
                    iconImage = Icons.TwoTone.Refresh,
                    iconSize = 18.dp,
                    iconTint = MaterialTheme.colors.onBackground,
                    contentDescription = "Refresh packages"
                )

                Spacer(modifier = Modifier.weight(1f))

                DynamicIconButton(
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier.size(36.dp),
                    onClickAction = {
                        ThemeState.isDarkMode.value = !isDarkMode
                    },
                    isEnabled = !isLoading,
                    iconImage = if (isDarkMode) Icons.TwoTone.WbSunny else Icons.TwoTone.ModeNight,
                    iconSize = 18.dp,
                    iconTint = MaterialTheme.colors.onBackground,
                    contentDescription = "Switch theme"
                )
            }

            errorMessage?.let {
                Text(
                    it,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 6.dp,
                    color = MaterialTheme.colors.onSurface
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.weight(1f),
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Version",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        fontFamily = bodyFont,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Divider(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(packages) { pkg ->
                        TableRowLayout(
                            pkg = pkg,
                            scope = scope,
                            setLoading = { isLoading = it },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PackageCard(pkg: model.Package) {

}