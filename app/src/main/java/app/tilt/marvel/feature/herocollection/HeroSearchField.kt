@file:OptIn(ExperimentalMaterial3Api::class)

package app.tilt.marvel.feature.herocollection

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.tilt.marvel.R
import timber.log.Timber

@Composable
fun HeroSearchField(searchValue: String, padding: Dp, onSearch: (String) -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    val source = remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val searchPadding by animateDpAsState(
        targetValue = if (focused) 0.dp else padding,
        animationSpec = tween()
    )
//        val isVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
//        LaunchedEffect(isVisible) {
//            Timber.tag("TEST").d("Keyboard visible $isVisible")
//            if (!isVisible) {
//                focusManager.clearFocus()
//            }
//        }
    OutlinedTextField(
        value = searchValue, onValueChange = onSearch, modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = searchPadding)
            .imePadding(),
        interactionSource = source,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            Timber.tag("TEST").d("on search")
            focusManager.clearFocus()
        }),
        trailingIcon = {
            if (searchValue.isNotEmpty())
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable { onSearch("") })
        },
        label = {
            Text(text = stringResource(R.string.search_hint))
        },
        colors = TextFieldDefaults.textFieldColors()
    )
}
