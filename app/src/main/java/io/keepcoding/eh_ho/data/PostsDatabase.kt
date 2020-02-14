package io.keepcoding.eh_ho.data


import androidx.room.*

@Entity(tableName = "latestpost_table")
data class LatestPostEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "topic_title") val topic_title: String,
    @ColumnInfo(name = "topic_slug") val topic_slug: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "post_title") val post_title: String,
    @ColumnInfo(name = "topic_id") val topicId: Int,
    @ColumnInfo(name = "post_date") val date: String,
    @ColumnInfo(name = "post_number") val post_number: Int
)

@Dao
interface PostDao {
    @Query("SELECT * FROM latestpost_table")
    fun getPosts(): List<LatestPostEntity>

    /*@Query("SELECT * FROM latestpost_table WHERE topic_id LIKE :id")
    fun getTopicById(id: String): TopicEntity*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(latestPostList: List<LatestPostEntity>): List<Long>

    @Delete
    fun delete(post: LatestPostEntity)
}

@Database(entities = [LatestPostEntity::class], version = 1)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}