package net.m3mobile.samples.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getStringExtra(CATEGORY_EXTRA)
            ?.let { name -> SampleCategory.entries.firstOrNull { it.name == name } }
            ?: run {
                finish()
                return
            }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CategoryScreen(category)
                }
            }
        }
    }

    companion object {
        private const val CATEGORY_EXTRA = "sample_category"

        internal fun intent(context: Context, category: SampleCategory): Intent =
            Intent(context, CategoryActivity::class.java)
                .putExtra(CATEGORY_EXTRA, category.name)
    }
}

@Composable
internal fun CategoryListScreen(onCategorySelected: (SampleCategory) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(R.string.category_list), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.select_category))
        SampleCategory.entries.forEach { category ->
            Button(
                onClick = { onCategorySelected(category) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(category.titleRes))
            }
        }
    }
}
