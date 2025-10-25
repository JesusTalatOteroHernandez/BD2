package mx.edu.utng.jtoh.bd2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.edu.utng.jtoh.bd2.data.local.dao.PostDao
import mx.edu.utng.jtoh.bd2.data.local.database.AppDataBase
import mx.edu.utng.jtoh.bd2.data.local.entity.Post
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDataBase
    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = AppDataBase.getInstance(this)
        postDao = db.postDao()

        setContent {
            MaterialTheme {
                PostScreen(postDao)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(postDao: PostDao) {
    val scope = rememberCoroutineScope()
    var posts by remember { mutableStateOf<List<Post>>(emptyList())}
    var text by remember { mutableStateOf("") }
    var editingPost by remember { mutableStateOf<Post?>(null) }

    //Cargar Lista
    LaunchedEffect(Unit) {
        posts = postDao.getAll()
    }
    fun refresh() {
        scope.launch { posts = postDao.getAll() }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CRUD con Room + Compose") }
            )
        },
        content = { padding ->
            Column (
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        textStyle = TextStyle(color =
                            MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    Button (onClick = {
                        scope.launch {
                            if (editingPost == null) {
                                postDao.insert(Post(content = text))
                            } else {
                                // Update (borrar y reinsertar)
                                postDao.delete(editingPost!!)
                                postDao.insert(Post(content = text))
                                editingPost = null
                            }
                            text = ""
                            refresh()
                        }
                    }) {
                        Text(if (editingPost == null) "Aggregar" else "Actualizar")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(posts) { post ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors =
                                CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement =
                                    Arrangement.SpaceBetween,
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Text(post.content)
                                Row {
                                    TextButton (onClick = {
                                        text = post.content
                                        editingPost = post
                                    }) {
                                        Text("Editar")
                                    }
                                    TextButton(onClick = {
                                        scope.launch {
                                            postDao.delete(post)
                                            refresh()
                                        }
                                    }) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}