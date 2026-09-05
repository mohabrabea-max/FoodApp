package com.example.applicationhome.features.settings.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.applicationhome.R
import com.example.applicationhome.core.domain.model.ProfileData
import com.example.applicationhome.core.domain.model.ProfileData.settings1
import com.example.applicationhome.core.domain.model.ProfileData.settings2
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.forCart.AlertDialogMessage
import com.example.applicationhome.core.ui.components.profileAndSetting.UserImage
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.core.ui.theme.MediumBrownForTitle
import com.example.applicationhome.data.data.model.AppLanguage
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.data.data.model.Settings
import com.example.applicationhome.data.data.model.SettingsConfirmDialog
import com.example.applicationhome.data.data.model.SettingsScreens
import com.example.applicationhome.data.data.model.ShowBottomSheets
import com.example.applicationhome.data.data.model.ThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    drawerState : DrawerState,
    coroutineScope : CoroutineScope,
    navigationController : NavHostController,
    dashboardNavController : NavHostController,
    settingsListState : LazyGridState,
    settingsViewModel : SettingsViewModel
){
    val userData by settingsViewModel.userData.collectAsStateWithLifecycle()
    val isLogin by settingsViewModel.isLogin.collectAsStateWithLifecycle()

    val confirmLogoutDialog by settingsViewModel.confirmLogoutDialog.collectAsStateWithLifecycle()
    val showBottomSheets by settingsViewModel.showBottomSheets.collectAsStateWithLifecycle()

    val currentThemeMode by settingsViewModel.currentThemeMode.collectAsStateWithLifecycle()

    val description = userData.phonenumber.ifEmpty { userData.email }

    val profileoptions = ProfileData.profileOptions()


    BackHandler(enabled = true) {
        dashboardNavController.navigate(Screens.HomeScreen.screen) {
            popUpTo(0) { inclusive = true }
        }
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        topBar = {
            Column(modifier = Modifier.shadow(elevation = 3.dp)){
                MyTopBar(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    title = stringResource(R.string.settings),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    startaction = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.size(50.dp).padding(5.dp).clip(CircleShape)
                        ){
                            Icon(
                                painterResource(id = R.drawable.custom_menu),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )

                if(isLogin) Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(Color.LightGray)
                ){
                    Row(
                        modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier.size(50.dp).
                            clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ){
                            UserImage()
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column(modifier = Modifier.weight(2.5f),horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center){
                            Text(
                                text = "${userData.firstname} ${userData.lastname}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.BrownForFont
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = description,
                                fontSize = 15.sp,
                                color = Color.MediumBrownForTitle
                            )
                        }
                    }
                }
            }
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp)
        ){
            LazyVerticalGrid(
                state = settingsListState,
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.Center
            ){
                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(15.dp))}

                items(profileoptions){ item ->
                    SettingsOptionsBox(
                        item
                    ){
                        when(item.screen){
                            Screens.Favorite.screen -> {
                                dashboardNavController.navigate(item.screen)
                            }

                            else -> {
                                navigationController.navigate(item.screen)
                            }
                        }
                    }
                }

                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(5.dp))}

                item(span = { GridItemSpan(2) }){
                    Row(modifier = Modifier.padding(start = 5.dp), horizontalArrangement = Arrangement.Start){
                        Text(
                            text = stringResource(R.string.settings),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                        )
                    }
                }

                item(span = { GridItemSpan(2) }){
                    SettingsBox(settings1()){
                        when(it){
                            SettingsScreens.DarkMode -> {
                                settingsViewModel.showDarkModeBottomSheet()
                            }

                            SettingsScreens.Language -> {
                                settingsViewModel.showLanguageBottomSheet()
                            }

                            SettingsScreens.AboutApp -> {

                            }

                            else -> {}
                        }
                    }
                }

                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(5.dp))}

                if(isLogin) item(span = { GridItemSpan(2) }){
                    SettingsBox(
                        settings = settings2(),
                        contentColor = Color.Red
                    ){
                        when(it){
                            SettingsScreens.Logout -> {
                                settingsViewModel.confirmLogout()
                            }

                            SettingsScreens.DeleteAccount -> {
                                settingsViewModel.confirmDeleteAccount()
                            }

                            else -> {}
                        }
                    }
                }

                if(!isLogin) item(span = { GridItemSpan(2) }){
                    SettingsBox(
                        settings = listOf(
                            Settings(
                                title = R.string.sign_in,
                                Icons.AutoMirrored.Filled.Login,
                                SettingsScreens.LogoIn
                            )
                        )
                    ){ navigationController.navigate(Screens.LoginScreen.screen) }
                }

                item(span = { GridItemSpan(2) }){Spacer(modifier = Modifier.height(80.dp))}
            }
        }

        when(val state = showBottomSheets){
            ShowBottomSheets.None -> {}

            ShowBottomSheets.Language -> {
                SettingsBottomSheet(
                    title = stringResource(R.string.language),
                    items = AppLanguage.entries,
                    isSelected = { language -> settingsViewModel.currentLanguage == language.code },
                    getItemLabel = { language -> language.titleRes },
                    onItemSelected = { language -> settingsViewModel.setAppLanguage(language.code) },
                    onDismissRequest = { settingsViewModel.closeBottomSheet() }
                )
            }

            ShowBottomSheets.DarkMode -> {
                SettingsBottomSheet(
                    title = stringResource(R.string.dark_mode),
                    items = ThemeMode.entries,
                    isSelected = { mode -> currentThemeMode == mode },
                    getItemLabel = { mode -> stringResource(mode.titleRes) },
                    onItemSelected = { mode -> settingsViewModel.updateAppTheme(mode) },
                    onDismissRequest = { settingsViewModel.closeBottomSheet() }
                )
            }
        }

        when(val state = confirmLogoutDialog){
            SettingsConfirmDialog.None -> {}

            is SettingsConfirmDialog.ConfirmLogout -> {
                AlertDialogMessage(
                    title = stringResource(R.string.disclaimer),
                    content = stringResource(state.message),
                    confirmButtonText = stringResource(R.string.logout),
                    confirmButton = {
                        settingsViewModel.closeDialog()
                        settingsViewModel.logout()
                    },
                    dismissButtonText = stringResource(R.string.cancel),
                    dismissButton = { settingsViewModel.closeDialog() }
                )
            }

            is SettingsConfirmDialog.ConfirmDeleteAccount -> {
                AlertDialogMessage(
                    title = stringResource(R.string.disclaimer),
                    content = stringResource(state.message),
                    confirmButtonText = stringResource(R.string.yes_i_m_sure),
                    confirmButton = {
                        settingsViewModel.closeDialog()
                        //--------------------------------------------------------
                    },
                    dismissButtonText = stringResource(R.string.cancel),
                    dismissButton = { settingsViewModel.closeDialog() }
                )
            }
        }
    }
}