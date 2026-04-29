package com.example.applicationhome.ui.theme.components

//@Composable
//fun CartButton(
//    modifier: Modifier = Modifier,
//    cartNumber : AddBoxViewModel = viewModel()
//){
//    val context = LocalContext.current
//    var cart2 = cartNumber.totalCart.value ?: 0
//    if(cart2 > 0){
//        Box(
//            modifier = Modifier.width(150.dp).
//            height(70.dp).
//            clip(shape = RoundedCornerShape(30.dp)).
//            background(Color.Blue).
//            clickable{
//                cartNumber.addToCart()
//                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
//            }.
//            border(width = 1.dp, color = Color.LightGray.copy(alpha = 1f), shape = RoundedCornerShape(30.dp)).
//            padding(5.dp),
//            contentAlignment = Alignment.Center
//        ){
//            Row(verticalAlignment = Alignment.CenterVertically){
//                Icon(
//                    Icons.Default.ShoppingCart,
//                    contentDescription = "Cart", tint = Color.White,
//                    modifier = Modifier.weight(1f)
//                )
//                VerticalDivider(color = Color.Black ,modifier = Modifier.align(Alignment.CenterVertically))
//                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
//                    Text(text = cart2.toString(), style = MaterialTheme.typography.labelLarge, color = Color.White)
//                }
//            }
//        }
//    }
//}