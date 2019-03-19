package com.bmacedo.hirememusic.util

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

fun NavController.navigateSafe(
    direction: NavDirections,
    extras: FragmentNavigator.Extras
) {
    val action = currentDestination?.getAction(direction.actionId)
    if (action != null) navigate(direction, extras)
}

fun NavController.navigateSafe(
    direction: NavDirections
) {
    val action = currentDestination?.getAction(direction.actionId)
    if (action != null) navigate(direction)
}