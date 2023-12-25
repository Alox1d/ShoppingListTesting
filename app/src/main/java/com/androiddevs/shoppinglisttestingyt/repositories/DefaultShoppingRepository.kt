package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDao
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.local.remote.PixabayAPI
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.utils.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            return if (response.isSuccessful) {
                response.body()?.let {
                    Resource.success(it)
                } ?: Resource.error("Response error: response body is null", null)
            } else {
                Resource.error("An server error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}