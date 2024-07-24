package com.davidnardya.moviesgallery

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.davidnardya.moviesgallery.dao.MoviesDao
import com.davidnardya.moviesgallery.db.MoviesDatabase
import com.davidnardya.moviesgallery.models.CachedImage
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.*
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@SmallTest
class MoviesDaoTest {

    private lateinit var database: MoviesDatabase
    private lateinit var moviesDao: MoviesDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java
        ).build()
        moviesDao = database.moviesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun `insert and get image`() = runBlocking {
        val cachedImage = CachedImage(url = "test_url", filePath = "test_path", cachedAt = System.currentTimeMillis())

        moviesDao.insertImage(cachedImage)
        val loaded = moviesDao.getImage("test_url")

        Assert.assertEquals(cachedImage, loaded)
    }

    @Test
    fun `delete expired images`() = runBlocking {
        val expiredTime = System.currentTimeMillis() - 100000
        val recentTime = System.currentTimeMillis() + 100000

        val expiredImage = CachedImage(url = "expired_url", filePath = "expired_path", cachedAt = expiredTime)
        val recentImage = CachedImage(url = "recent_url", filePath = "recent_path", cachedAt = recentTime)

        moviesDao.insertImage(expiredImage)
        moviesDao.insertImage(recentImage)

        moviesDao.deleteExpiredImages(System.currentTimeMillis())

        val loaded = moviesDao.getImage("recent_url")
        Assert.assertEquals(recentImage, loaded)

        val deleted = moviesDao.getImage("expired_url")
        Assert.assertNull(deleted)
    }
}

