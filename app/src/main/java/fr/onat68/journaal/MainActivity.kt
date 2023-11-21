package fr.onat68.journaal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fr.onat68.journaal.EntriesRepository.Singleton.entriesList
import fr.onat68.journaal.mainView.MainViewScreen
import fr.onat68.journaal.newEntry.NewEntryScreen
import fr.onat68.journaal.newEntry.NewEntryViewModel
import fr.onat68.journaal.readEntry.CardDetails


class MainActivity : ComponentActivity() {
    var context = this

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = EntriesRepository()
        repo.set()

        setContent {


            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "lastEntries") {
                navigation(
                    startDestination = "entries",
                    route = "lastEntries"
                ) {
                    composable("entries") {
                        MainViewScreen(navController, repo)
                    }
                    composable(
                        "details/{entryIndex}/{entryHue}",
                        arguments = listOf(navArgument("entryIndex") {
                            type = NavType.IntType
                        }, navArgument("entryHue") { type = NavType.FloatType })
                    ) {
                        val entryIndex = it.arguments?.getInt("entryIndex") ?: -1
                        val entryHue = it.arguments?.getFloat("entryHue")
                        CardDetails(entriesList[entryIndex]!!, entryHue!!)
                    }
                    composable("addEntry") {
                        val newEntryViewModel = NewEntryViewModel()
                        NewEntryScreen(repo, newEntryViewModel, navController)
                    }
                }
            }
        }

    }
}





