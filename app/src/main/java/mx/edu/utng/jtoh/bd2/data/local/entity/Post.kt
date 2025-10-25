package mx.edu.utng.jtoh.bd2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String

)