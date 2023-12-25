package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingDaoTest {

    // rule swaps the background executor
    // used by the Architecture Components
    // with a different one which executes each task synchronously.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        )
            .allowMainThreadQueries() // to execute all inside only one thread
            .build()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runTest(mainDispatcherRule.testDispatcher) {
        val item = ShoppingItem(
            name = "Banan",
            1,
            1f,
            "i",
            1
        )
        dao.insertShoppingItem(item)
        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        Assert.assertEquals(item, allShoppingItems[0])
    }

    @Test
    fun deleteShoppingItem() = runTest(mainDispatcherRule.testDispatcher) {
        val item = ShoppingItem(
            name = "Banan",
            1,
            1f,
            "i",
            1
        )
        dao.insertShoppingItem(item)
        dao.deleteShoppingItem(item)
        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        Assert.assertEquals(true, allShoppingItems.isEmpty())
    }

    @Test
    fun observeTotalPriceSum() = runTest(mainDispatcherRule.testDispatcher) {
        val item1 = ShoppingItem(
            name = "Banan",
            1,
            1f,
            "i",
            1
        )
        val item2 = ShoppingItem(
            name = "Banan",
            2,
            2f,
            "i",
            2
        )
        val item3 = ShoppingItem(
            name = "Banan",
            3,
            3f,
            "i",
            3
        )
        dao.insertShoppingItem(item1)
        dao.insertShoppingItem(item2)
        dao.insertShoppingItem(item3)
        val allShoppingItems = dao.observeTotalPrice().getOrAwaitValue()

        Assert.assertEquals(
            item1.price * item1.amount
                    + item2.price * item2.amount
                    + item3.price * item3.amount,
            allShoppingItems
        )
    }
}