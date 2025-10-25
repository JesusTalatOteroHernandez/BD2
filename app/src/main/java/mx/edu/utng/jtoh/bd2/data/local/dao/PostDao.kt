package mx.edu.utng.jtoh.bd2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.jtoh.bd2.data.local.entity.Post


@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): List<Post>

    @Insert
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("SELECT * FROM posts WHERE id = id")
    suspend fun getById(id: Int): Post?

    @Update
    suspend fun update(post: Post)

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: Int): Flow<Post>

    @Query("DELETE FROM posts")
    suspend fun deleteAll()

}