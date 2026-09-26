package com.example.applicationhome.features.restaurantscreen.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.theme.DeepMatteBlack

@Composable
fun DiscountsBoxForRestaurantScreen(
    discountText : String,
    onViewItemsClick : () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    val leftBgColor = Color(0xFFF9F3EB)     // بيج فاتح
    val rightBgColor = Color(0xFFE4D5C2)    // بيج دافئ (للجهة اليمنى)
    val highlightColor = Color(0xFFD2F84A)  // ليموني نيون
    val textColor = Color.DeepMatteBlack


    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .width(300.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(15.dp))
            .drawBehind {
                drawRect(color = leftBgColor)

                val slopePath = Path().apply {
                    moveTo(size.width * 0.60f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(size.width * 0.55f, size.height)
                    close()
                }
                drawPath(path = slopePath, color = rightBgColor)
            }
            .padding(15.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        // ------------------ الجزء الأيسر (تفاصيل الخصم) ------------------
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ){
            val offerAnnotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        background = highlightColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = textColor
                    )
                ){
                    append(" $discountText ")
                }

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = textColor
                    )
                ){
                    append(" " + stringResource(R.string.off_discount))
                }
            }

            Text(text = offerAnnotatedString)

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(R.string.select_items),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // ------------------ الجزء الأيمن (الأيقونة والرابط) ------------------

        Text(
            text = stringResource(R.string.view_items),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ){ onViewItemsClick() }
        )
    }
}