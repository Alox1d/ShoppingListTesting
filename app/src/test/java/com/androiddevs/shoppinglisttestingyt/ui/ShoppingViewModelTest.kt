package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.MainDispatcherRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepository
import com.androiddevs.shoppinglisttestingyt.utils.Consts
import com.androiddevs.shoppinglisttestingyt.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest {

    // rule swaps the background executor
    // used by the Architecture Components
    // with a different one which executes each task synchronously.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // WE DON'T HAVE MAIN DISPATCHER IN TESTS!
    // So we NEED to REPLACE dispatcher with special dispatcher
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        viewModel.insertShoppingItem("name", "", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val string = buildString {
            for (i in 0..Consts.MAX_NAME_LENGTH + 1) {
                append(i)
            }
        }
        viewModel.insertShoppingItem(
            string,
            "",
            "3.0"
        )

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val string = buildString {
            for (i in 0..Consts.MAX_PRICE_LENGTH + 1) {
                append(i)
            }
        }
        viewModel.insertShoppingItem(
            "string",
            "",
            string
        )

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {
        viewModel.insertShoppingItem(
            "string",
            "9999999999999999999999999999999999999999999",
            "1.0"
        )

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem(
            "name",
            "5",
            "1.0"
        )

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        Assert.assertEquals(Status.SUCCESS, value.getContentIfNotHandled()?.status)
    }
}