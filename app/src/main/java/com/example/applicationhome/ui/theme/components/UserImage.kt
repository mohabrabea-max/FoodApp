package com.example.applicationhome.ui.theme.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.applicationhome.ui.theme.model.UserImageViewModel

@Composable
fun UserImage(userImageViewModel : UserImageViewModel){
    var selectedImageUri = userImageViewModel.selectedImageUri.value
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            // أول ما المستخدم يختار صورة، بنسجل الـ Uri بتاعها هنا
            userImageViewModel.addPhoto(uri)
        }
    )
    Box(
        modifier = Modifier.
            size(120.dp). // حجم مكان الصورة
            clip(CircleShape). // عشان نخليها دائرية تماماً
            background(Color.LightGray). // لون خلفية لو مفيش صورة
            clickable {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }.
            border(width = 0.5.dp, color = Color.Gray.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ){
        if(selectedImageUri != null){
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }else{
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Profile Icon",
                modifier = Modifier.size(40.dp), // حجم الأيقونة الافتراضية
                tint = Color.Gray
            )
        }
    }
}