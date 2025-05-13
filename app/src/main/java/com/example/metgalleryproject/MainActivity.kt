package com.example.metgalleryproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metgalleryproject.data.network.MetMuseumApiService
import com.example.metgalleryproject.repository.MetRepository
import com.example.metgalleryproject.ui.components.BottomNavBar
import com.example.metgalleryproject.ui.components.TopNavBar
import com.example.metgalleryproject.ui.screens.DetailsScreen
import com.example.metgalleryproject.ui.screens.FavouritesScreen
import com.example.metgalleryproject.ui.screens.HomeScreen
import com.example.metgalleryproject.ui.screens.SearchScreen
import com.example.metgalleryproject.ui.theme.MetGalleryProjectTheme
import com.example.metgalleryproject.viewmodel.DetailsViewModel
import com.example.metgalleryproject.viewmodel.DetailsViewModelFactory
import com.example.metgalleryproject.viewmodel.FavouritesViewModel
import com.example.metgalleryproject.viewmodel.HomeViewModel
import com.example.metgalleryproject.viewmodel.SearchViewModel
import com.example.metgalleryproject.viewmodel.SearchViewModelFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetGalleryProjectTheme {

                val navController = rememberNavController()
                val favouritesViewModel: FavouritesViewModel = viewModel()
                val homeViewModel: HomeViewModel = viewModel()

                val repository = MetRepository(MetMuseumApiService.api)
                val viewModelFactory = SearchViewModelFactory(repository)
                val searchViewModel = viewModels<SearchViewModel> { viewModelFactory }.value

                val detailsViewModelFactory = DetailsViewModelFactory(repository)

                Scaffold(
                    topBar = { TopNavBar() },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { paddingValues ->

                    NavHost(
                        navController = navController,
                        startDestination = "home",
                                modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("home") {
                            HomeScreen(navController = navController, viewModel = homeViewModel, favouritesViewModel = favouritesViewModel)
                        }
                        composable("search") {
                            SearchScreen(navController = navController, viewModel = searchViewModel, favouritesViewModel = favouritesViewModel)
                        }
                        composable("favourites") {
                            FavouritesScreen(navController = navController, viewModel = favouritesViewModel)
                        }
                        composable("details/{artId}") { backStackEntry ->
                            val artId = backStackEntry.arguments?.getString("artId")?.toIntOrNull() ?: 0
                            val detailsViewModel: DetailsViewModel = viewModel(
                                factory = DetailsViewModelFactory(repository)
                            )
                            detailsViewModel.fetchArtDetails(artId)
                            DetailsScreen(artId = artId, viewModel = detailsViewModel,
                                favouritesViewModel = favouritesViewModel)
                        }
                    }
                }
            }
        }
    }
}