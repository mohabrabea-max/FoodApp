package com.example.applicationhome.features.homescreen.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import com.example.applicationhome.data.data.model.HomeScreenActions
import com.example.applicationhome.data.data.model.HomeScreenParameters
import com.example.applicationhome.data.data.model.HomeUiState
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val testRule : ComposeContentTestRule = createComposeRule()

    @Test
    fun loadingState_isActive(){
        testRule.setContent {
            HomeScreen(
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                coroutineScope = rememberCoroutineScope(),
                navigationController = rememberNavController(),
                onActions = HomeScreenActions(),
                parameters = HomeScreenParameters(),
                scrollState = rememberLazyListState(),
                syncDataUiState = HomeUiState.Loading,
                isRefreshing = false,
                onRefresh = {  }
            )
        }

        testRule.onNodeWithTag("shimmer").assertIsDisplayed()
    }
}