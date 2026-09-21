package com.example.applicationhome.features.homescreen.ui

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

class HomeScreenTest {
    @get:Rule
    val testRule : ComposeContentTestRule = createComposeRule()

//    @Test
//    fun loadingState_isActive(){
//        testRule.setContent {
//            HomeScreen(
//                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
//                coroutineScope = rememberCoroutineScope(),
//                navigationController = rememberNavController(),
//                onActions = HomeScreenActions(),
//                parameters = HomeScreenParameters(),
//                scrollState = rememberLazyListState(),
//                syncDataUiState = UiStates.Loading,
//                isRefreshing = false,
//                onRefresh = { },
//                startBottomSheets = StartBottomSheets
//            )
//        }
//
//        testRule.onNodeWithTag("shimmer").assertIsDisplayed()
//    }
}