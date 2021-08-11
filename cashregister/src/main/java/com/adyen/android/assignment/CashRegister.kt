package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import com.adyen.android.assignment.money.MonetaryElement

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {

        //If amountPaid is less than the price
        if(price > amountPaid.total) throw TransactionException("Amount paid is less than total price.")

        //If current cash balance is less than the amountPaid
        if(change.total < amountPaid.total) throw TransactionException("Insufficient cash!")

        //If price and amountPaid are equal
        if(price == amountPaid.total) return Change.none()

        return calculateChange(amountPaid.total - price)
    }

    /**
     * The calculateChange method calculate the change which will be return to the customer.
     *
     * @param cashDiff The Difference between the amount paid by the customer and total amount to be paid
     *
     * @return Change, the customer will receive
     *
     * @throws TransactionException If exact change is not available
     */
    private fun calculateChange(cashDiff: Long): Change {
        var cashBack = cashDiff
        val resultChange = Change()
        var moneyType: MonetaryElement
        loop@ while(cashBack > 0) {
            when {
                //Euro
                cashBack >= Bill.FIVE_HUNDRED_EURO.minorValue && change.getCount(Bill.FIVE_HUNDRED_EURO) > 0 -> moneyType = Bill.FIVE_HUNDRED_EURO
                cashBack >= Bill.TWO_HUNDRED_EURO.minorValue && change.getCount(Bill.TWO_HUNDRED_EURO) > 0 -> moneyType = Bill.TWO_HUNDRED_EURO
                cashBack >= Bill.ONE_HUNDRED_EURO.minorValue && change.getCount(Bill.ONE_HUNDRED_EURO) > 0 -> moneyType = Bill.ONE_HUNDRED_EURO
                cashBack >= Bill.FIFTY_EURO.minorValue && change.getCount(Bill.FIFTY_EURO) > 0 -> moneyType = Bill.FIFTY_EURO
                cashBack >= Bill.TWENTY_EURO.minorValue && change.getCount(Bill.TWENTY_EURO) > 0 -> moneyType = Bill.TWENTY_EURO
                cashBack >= Bill.TEN_EURO.minorValue && change.getCount(Bill.TEN_EURO) > 0 -> moneyType = Bill.TEN_EURO
                cashBack >= Bill.FIVE_EURO.minorValue && change.getCount(Bill.FIVE_EURO) > 0 -> moneyType = Bill.FIVE_EURO
                cashBack >= Coin.TWO_EURO.minorValue && change.getCount(Coin.TWO_EURO) > 0 -> moneyType = Coin.TWO_EURO
                cashBack >= Coin.ONE_EURO.minorValue && change.getCount(Coin.ONE_EURO) > 0 -> moneyType = Coin.ONE_EURO

                //Cents
                cashBack >= Coin.FIFTY_CENT.minorValue && change.getCount(Coin.FIFTY_CENT) > 0 -> moneyType = Coin.FIFTY_CENT
                cashBack >= Coin.TWENTY_CENT.minorValue && change.getCount(Coin.TWENTY_CENT) > 0 -> moneyType = Coin.TWENTY_CENT
                cashBack >= Coin.TEN_CENT.minorValue && change.getCount(Coin.TEN_CENT) > 0 -> moneyType = Coin.TEN_CENT
                cashBack >= Coin.FIVE_CENT.minorValue && change.getCount(Coin.FIVE_CENT) > 0 -> moneyType = Coin.FIVE_CENT
                cashBack >= Coin.TWO_CENT.minorValue && change.getCount(Coin.TWO_CENT) > 0 -> moneyType = Coin.TWO_CENT
                cashBack >= Coin.ONE_CENT.minorValue && change.getCount(Coin.ONE_CENT) > 0 -> moneyType = Coin.ONE_CENT

                //When right combination of change are not present
                else -> throw TransactionException("Insufficient Change!")
            }

            resultChange.add(moneyType, 1)
            change.remove(moneyType, 1)
            cashBack -= moneyType.minorValue
        }

        return resultChange
    }

    class TransactionException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
