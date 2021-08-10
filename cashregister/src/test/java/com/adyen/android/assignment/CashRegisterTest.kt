package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import org.junit.Assert.*
import org.junit.Test

class CashRegisterTest {

    @Test
    fun testLessAmountPaidTransaction() {
        val shoppersMoney = Change().add(Bill.FIVE_HUNDRED_EURO, 1)
        val exception = assertThrows(CashRegister.TransactionException::class.java) {
            CashRegister(Change.max()).performTransaction(1200_00, shoppersMoney)
        }
        assertEquals("Amount paid is less than total price.", exception.message)
    }

    @Test
    fun testInsufficientCashTransaction() {
        val change = Change()
                .add(Bill.TEN_EURO, 4)

        val shoppersMoney = Change().add(Bill.FIVE_HUNDRED_EURO, 3)
        val exception = assertThrows(CashRegister.TransactionException::class.java) {
            CashRegister(change).performTransaction(1200_00, shoppersMoney)
        }
        assertEquals("Insufficient cash!", exception.message)
    }

    @Test
    fun testInsufficientChangeTransaction() {
        val change = Change()
                .add(Bill.FIVE_HUNDRED_EURO, 1)

        val shoppersMoney = Change().add(Bill.ONE_HUNDRED_EURO, 3)
        val exception = assertThrows(CashRegister.TransactionException::class.java) {
            CashRegister(change).performTransaction(250, shoppersMoney)
        }
        assertEquals("Insufficient Change!", exception.message)
    }

    @Test
    fun testZeroChangeTransaction() {
        val shoppersMoney = Change().add(Bill.FIVE_HUNDRED_EURO, 4)
        val result =  CashRegister(Change.max()).performTransaction(2000_00, shoppersMoney)
        assertEquals(0, result.total)
    }

    @Test
    fun testTransaction() {
        val shoppersMoney = Change().add(Bill.FIVE_HUNDRED_EURO, 3)
        val result = CashRegister(Change.max()).performTransaction(1200_00, shoppersMoney)

        assertEquals(300_00, result.total)
    }
}
