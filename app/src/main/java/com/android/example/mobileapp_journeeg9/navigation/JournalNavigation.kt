package com.android.example.mobileapp_journeeg9.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.*
import com.android.example.mobileapp_journeeg9.ui.screens.*
import com.android.example.mobileapp_journeeg9.ui.util.*
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

sealed class Screen(val route: String) {
    object Journal : Screen("journal")
    object Calendar : Screen("calendar")
    object Media : Screen("media")
    object Atlas : Screen("atlas")
    object Profile : Screen("profile")
    object CreateEntry : Screen("create_entry")
    object EntryDetail : Screen("entry_detail/{entryId}") {
        fun createRoute(entryId: String) = "entry_detail/$entryId"
    }
    object EditEntry : Screen("edit_entry/{entryId}") {
        fun createRoute(entryId: String) = "edit_entry/$entryId"
    }
}

@Composable
fun JournalNavigation(
    navController: NavHostController,
    windowStateUtil: WindowStateUtil,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (windowStateUtil.shouldShowBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Book, "Journal") },
                        label = { Text("Journal") },
                        selected = currentRoute == Screen.Journal.route,
                        onClick = { navController.navigate(Screen.Journal.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.DateRange, "Calendar") },
                        label = { Text("Calendar") },
                        selected = currentRoute == Screen.Calendar.route,
                        onClick = { navController.navigate(Screen.Calendar.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Image, "Media") },
                        label = { Text("Media") },
                        selected = currentRoute == Screen.Media.route,
                        onClick = { navController.navigate(Screen.Media.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Place, "Atlas") },
                        label = { Text("Atlas") },
                        selected = currentRoute == Screen.Atlas.route,
                        onClick = { navController.navigate(Screen.Atlas.route) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Row(modifier = modifier.padding(paddingValues)) {
            if (windowStateUtil.shouldShowNavRail) {
                NavigationRail {
                    NavigationRailItem(
                        icon = { Icon(Icons.Default.Book, "Journal") },
                        label = { Text("Journal") },
                        selected = currentRoute == Screen.Journal.route,
                        onClick = { navController.navigate(Screen.Journal.route) }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Default.DateRange, "Calendar") },
                        label = { Text("Calendar") },
                        selected = currentRoute == Screen.Calendar.route,
                        onClick = { navController.navigate(Screen.Calendar.route) }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Default.Image, "Media") },
                        label = { Text("Media") },
                        selected = currentRoute == Screen.Media.route,
                        onClick = { navController.navigate(Screen.Media.route) }
                    )
                    NavigationRailItem(
                        icon = { Icon(Icons.Default.Place, "Atlas") },
                        label = { Text("Atlas") },
                        selected = currentRoute == Screen.Atlas.route,
                        onClick = { navController.navigate(Screen.Atlas.route) }
                    )
                }
            }

            NavHost(
                navController = navController,
                startDestination = Screen.Journal.route,
                modifier = Modifier.weight(1f)
            ) {
                composable(Screen.Journal.route) {
                    JournalScreen(
                        onNewEntry = { navController.navigate(Screen.CreateEntry.route) },
                        onEntryClick = { entryId ->
                            if (windowStateUtil.shouldShowTwoPanes) {
                                // Show entry detail in second pane
                                navController.navigate(Screen.EntryDetail.createRoute(entryId))
                            } else {
                                // Navigate to entry detail screen
                                navController.navigate(Screen.EntryDetail.createRoute(entryId))
                            }
                        },
                        onProfileClick = { navController.navigate(Screen.Profile.route) }
                    )
                }

                composable(Screen.Calendar.route) {
                    CalendarScreen(
                        onDateSelected = { date ->
                            // TODO: Show entries for selected date
                        },
                        journalDates = setOf(LocalDate.now()) // TODO: Get actual journal dates
                    )
                }

                composable(Screen.Media.route) {
                    MediaScreen(
                        journalEntries = emptyList(), // TODO: Get actual entries with media
                        onMediaClick = { mediaUrl ->
                            // TODO: Navigate to entry with this media
                        }
                    )
                }

                composable(Screen.Atlas.route) {
                    AtlasScreen(
                        journalMarkers = emptyList(), // TODO: Get actual entries with location
                        onMarkerClick = { entry ->
                            navController.navigate(Screen.EntryDetail.createRoute(entry.id))
                        }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        username = "Samuel",
                        email = "galaxywanderer157@email.com",
                        phone = "+62 812-3456-7890",
                        address = "789 Starry Lane, Nebula Heights, Jakarta",
                        dateOfBirth = "June 15, 1997",
                        profilePicUrl = null,
                        onEditProfile = { /* TODO: Implement edit profile */ },
                        onLogout = { /* TODO: Implement logout */ }
                    )
                }

                composable(Screen.CreateEntry.route) {
                    CreateEntryScreen(
                        onBack = { navController.popBackStack() },
                        onSave = { title, description ->
                            // TODO: Save entry
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = Screen.EntryDetail.route,
                    arguments = listOf(
                        navArgument("entryId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val entryId = backStackEntry.arguments?.getString("entryId")
                    EntryDetailScreen(
                        title = "Sample Title", // TODO: Get actual entry
                        description = "Sample Description",
                        onBack = { navController.popBackStack() },
                        onEdit = { navController.navigate(Screen.EditEntry.createRoute(entryId!!)) }
                    )
                }

                composable(
                    route = Screen.EditEntry.route,
                    arguments = listOf(
                        navArgument("entryId") { type = NavType.StringType }
                    )
                ) {
                    EditEntryScreen(
                        initialTitle = "Sample Title", // TODO: Get actual entry
                        initialDescription = "Sample Description",
                        onBack = { navController.popBackStack() },
                        onSave = { title, description ->
                            // TODO: Save edited entry
                            navController.popBackStack()
                        },
                        onDelete = {
                            // TODO: Delete entry
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
