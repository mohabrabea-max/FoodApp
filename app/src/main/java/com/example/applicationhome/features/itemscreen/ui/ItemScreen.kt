package com.example.applicationhome.features.itemscreen.ui

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ItemScreen(
//    navigationController : NavHostController,
//    itemScreenViewModel : ItemScreenViewModel
//){
//    val snackbarHostState = remember { SnackbarHostState() }
//    val scope = rememberCoroutineScope()
//
//    val errorInCart by itemScreenViewModel.errorInCart.collectAsStateWithLifecycle()
//
//    val userData by itemScreenViewModel.userData.collectAsStateWithLifecycle()
//
//    val cartInformation by itemScreenViewModel.cartInformation.collectAsStateWithLifecycle()
//
//    val newCount by itemScreenViewModel.newCount.collectAsStateWithLifecycle()
//
//    val scrollState = rememberLazyListState()
//
//    val item by itemScreenViewModel.selectedMeal.collectAsStateWithLifecycle()
//    val size by itemScreenViewModel.mealSize.collectAsStateWithLifecycle()
//
//    val price = item?.meal?.sizeOptions?.find { it.size == size }?.price ?: 0.0
//
//
//    Scaffold(
//        modifier = Modifier.navigationBarsPadding().fillMaxSize(),
//        snackbarHost = {
//            SnackbarHost(hostState = snackbarHostState){ data ->
//                Snackbar(
//                    containerColor = Color.DeepMatteBlack,
//                    actionColor = Color.DarkOrange,
//                    snackbarData = data,
//                    shape = RoundedCornerShape(15.dp)
//                )
//            }
//            Spacer(modifier = Modifier.height(160.dp))
//        },
//        topBar = {
//            ItemScreenTopBar(
//                scrollState,
//                item?.isFavorite ?: false,
//                {
//                    if (navigationController.previousBackStackEntry != null) {
//                        navigationController.popBackStack()
//                    }
//                },
//                {
//                    navigationController.navigate(Screens.Search.screen) {
//                        popUpTo(navigationController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//                {
//                    val favoriteFoodDatabase =
//                        FavoriteMealEntity(
//                            item?.meal?.id ?: 0,
//                            userData.id,
//                            item?.meal?.restaurantId ?: 0,
//                            false,
//                            false
//                        )
//                    itemScreenViewModel.addMealFavorite(favoriteFoodDatabase)
//                },
//                { itemScreenViewModel.removeMealFavorite(item?.meal?.id ?: 0) }
//            )
//        }
//    ){
//        Box(modifier = Modifier.background(Color.VeryLightGray).padding(10.dp)){
//            LazyColumn(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                state = scrollState,
//                modifier = Modifier.fillMaxSize()
//            ){
//                item{
//                    Column{
//                        Spacer(modifier = Modifier.height(50.dp))
//                        ItemScreenImage(
//                            scrollState,
//                            item?.meal?.image ?: ""
//                        )
//                    }
//                }
//                item {
//                    //Spacer(modifier = Modifier.height(20.dp))
//                    Column(
//                        modifier = Modifier.
//                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
//                        fillMaxWidth().
//                        background(Color.White).
//                        padding(15.dp)
//                    ){
//                        Text(
//                            text = item?.meal?.name ?: "",
//                            fontSize = 20.sp,
//                            style = MaterialTheme.typography.labelLarge,
//                            color = Color.BrownForFont,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Spacer(modifier = Modifier.height(10.dp))
//
//                        Text(
//                            text = item?.meal?.details ?: "",
//                            color = Color.MediumBrownForTitle
//                        )
//
//                        Spacer(modifier = Modifier.height(10.dp))
//
//                        Text(
//                            text = "$price L.E",
//                            fontSize = 30.sp,
//                            style = MaterialTheme.typography.labelLarge,
//                            color = Color.BrownForFont,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(start = 15.dp, bottom = 15.dp)
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Box(
//                        modifier = Modifier.
//                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
//                        fillMaxWidth().
//                        background(Color.White)
//                    ){
//                        Column{
//                            Spacer(modifier = Modifier.height(15.dp))
//                            Text(
//                                text = "Meal snacks",
//                                fontSize = 16.sp,
//                                color = Color.Black,
//                                style = MaterialTheme.typography.labelLarge,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.padding(start = 15.dp)
//                            )
//                            Spacer(modifier = Modifier.height(5.dp))
//                            LazyRow {
//                                item{Spacer(modifier = Modifier.width(7.dp))}
//                                    item{
//                                        val selectedDetail = item?.meal?.sizeOptions?.find { it.size == size }
//                                        selectedDetail?.snack?.forEach { (snakeId, value) ->
//                                            SnaksBoxForItemScreen(
//                                                modifier = Modifier.size(170.dp),
//                                                value
//                                            )
//                                        }
//                                    }
//                                item{Spacer(modifier = Modifier.width(7.dp))}
//                            }
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Box(
//                        modifier = Modifier.
//                        shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(20.dp)).
//                        fillMaxWidth().
//                        background(Color.White).
//                        padding(10.dp)
//                    ){
//                        ItemSize(
//                            item?.meal?.sizeOptions ?: emptyList(),
//                            size
//                        ) { selectedSize ->
//                            if(item != null) itemScreenViewModel.selectItem(item!!, selectedSize)
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    RatingsAndReviews(
//                        item?.meal?.review ?: 0.0
//                    )
//                }
//                item{Spacer(modifier = Modifier.height(150.dp))}
//            }
//        }
//        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){
//            val price = item?.meal?.sizeOptions?.find { it.size == size }?.price ?: 0.0
//            val totalPrice = newCount * price
//            BottomBarForItemScreen(
//                price,
//                newCount,
//                { itemScreenViewModel.minusnewCount() },
//                { itemScreenViewModel.plusnewCount() },
//                {
//                    val meal = CartItemsClass(
//                        userData.id,
//                        "${item?.meal?.id}_${size}",
//                        item?.meal?.id ?: 0,
//                        item?.meal?.name ?: "",
//                        item?.meal?.category ?: "",
//                        size,
//                        newCount,
//                        price,
//                        totalPrice,
//                        item?.meal?.image ?: "",
//                        item?.meal?.restaurantId ?: 0
//                    )
//
//                    itemScreenViewModel.updateCount(
//                        meal,
//                        size,
//                        newCount,
//                        cartError = {
//                            if ((item?.meal?.restaurantId == cartInformation?.restaurantId || cartInformation == null)){
//                                itemScreenViewModel.deletenewCount()
//                                scope.showAddToCartSnackbar(
//                                    snackbarHostState,
//                                    {
//                                        navigationController.navigate(Screens.Cart.screen) {
//                                            launchSingleTop = true
//                                        }
//                                    }
//                                )
//                            }
//                        }
//                    )
//                }
//            )
//        }
//
//        if(errorInCart.first && errorInCart.second.isEmpty()){
//            AlertDialogMessage(
//                "Start a new cart?",
//                "A new order will clear your cart with '${cartInformation?.restaurantName ?: ""}'",
//                "Start",
//                {
//                    itemScreenViewModel.alertDialogFalse()
//                    itemScreenViewModel.clearAndStartNewCart(newCount)
//                    itemScreenViewModel.deletenewCount()
//                    scope.showAddToCartSnackbar(
//                        snackbarHostState,
//                        {
//                            navigationController.navigate(Screens.Cart.screen) {
//                                launchSingleTop = true
//                            }
//                        }
//
//                    )
//                },
//                "Cancel",
//                { itemScreenViewModel.alertDialogFalse() }
//            )
//        }else if(errorInCart.first){
//            AlertDialogMessage(
//                "Sign in required!",
//                "Please sign in or create an account to add items to your cart and proceed with your order.",
//                "Sign in",
//                {
//                    navigationController.navigate(Screens.LoginScreen.screen)
//                    itemScreenViewModel.alertDialogFalse()
//                },
//                "Cancel",
//                { itemScreenViewModel.alertDialogFalse() }
//            )
//        }
//    }
//}