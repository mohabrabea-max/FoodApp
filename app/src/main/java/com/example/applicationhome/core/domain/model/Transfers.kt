package com.example.applicationhome.core.domain.model

import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.model.Snack
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SnacksEntity

fun MealsEntity.mealsEntityToFoodItem(): FoodItem =
    FoodItem(
        this.id,
        this.category,
        this.name,
        this.details,
        this.image,
        this.sizeOptions,
        this.restaurantId,
        this.review
    )

fun FoodItem.foodItemToMealsEntity(): MealsEntity =
    MealsEntity(
        this.id,
        this.category,
        this.name,
        this.details,
        this.image,
        this.sizeOptions,
        this.restaurantId,
        this.review
    )

fun Snack.snackToSnacksEntity(): SnacksEntity =
    SnacksEntity(
        this.id,
        this.name,
        this.details,
        this.image,
        this.priceANDsize,
        this.restaurantId,
        this.review
    )


fun RestaurantsEntity.restaurantsEntityToRestaurants(): Restaurants =
    Restaurants(
        this.id,
        this.typ,
        this.name,
        this.image,
        this.image2,
        this.review,
        this.background,
        this.searchKeywords,
        this.topFiveMeals
    )

fun Restaurants.restaurantsToRestaurantsEntity(): RestaurantsEntity =
    RestaurantsEntity(
        this.id,
        this.name,
        this.typ,
        this.image,
        this.image2,
        this.review,
        this.background,
        this.searchKeywords,
        this.topFiveMeals
    )