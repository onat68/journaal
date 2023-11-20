package fr.onat68.journaal.mainView

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.onat68.journaal.EntriesRepository

@Composable
fun MainViewScreen(navController: NavController, repo: EntriesRepository) {
    val isLoading by EntriesRepository.Singleton.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    Column {
        LastEntries(navController)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                repo.set()
            }) {
            EntriesList(EntriesRepository.Singleton.entriesList, navController)
        }
    }
}