package mx.edu.utng.jtoh.bd2.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.edu.utng.jtoh.bd2.data.local.entity.Post
import mx.edu.utng.jtoh.bd2.data.repository.PostRepository

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    //Devuelve todass las publicaciones
    val posts: StateFlow<List<Post>> = repository.posts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    //Agruegar nueva pubilicacion
    fun addPost(content: String){
        viewModelScope.launch {
            val post = Post(content = content)
            repository.insert(post)
        }
    }

    //Eliminar una publicacion
    fun deletePost(post: Post){
        viewModelScope.launch {
            repository.delete(post)
        }
    }

    //Actualizar una publicacion
    fun updatePost(content: String){
        viewModelScope.launch {
            val post = Post(content = content)
            repository.update(post)
        }
    }

    //Borrar todas las publicaciones
    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    //Obtener un elemento especifico
    fun getPostById(postId: Int): Flow<Post?> {
        return repository.getPostById(postId)
    }
}



