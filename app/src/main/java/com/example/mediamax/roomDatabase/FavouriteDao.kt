package com.example.mediamax.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favourite)

    @Query("SELECT EXISTS(SELECT 1 FROM favourite WHERE path=:filePath)")
    suspend fun isPathExists(filePath: String): Boolean

    @Query("SELECT * FROM favourite")
   suspend  fun getFavouriteData(): List<Favourite>

    @Query("DELETE FROM Favourite WHERE path = :filePath")
    suspend fun deleteFavorite(filePath: String)

    @Update
    suspend fun updateFavorite(favorite: Favourite)

}
