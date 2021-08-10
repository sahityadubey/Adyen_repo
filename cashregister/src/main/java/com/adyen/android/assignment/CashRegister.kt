package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin

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

    private fun calculateChange(cashDiff: Long): Change {
        var cashBack = cashDiff
        val resultChange = Change()
        while(cashBack > 1) {
            when {
                //Euro
                cashBack > Bill.FIVE_HUNDRED_EURO.minorValue && change.getCount(Bill.FIVE_HUNDRED_EURO) > 0 -> {
                    resultChange.add(Bill.FIVE_HUNDRED_EURO, 1)
                    change.remove(Bill.FIVE_HUNDRED_EURO, 1)
                    cashBack -= Bill.FIVE_HUNDRED_EURO.minorValue
                }
                cashBack > Bill.TWO_HUNDRED_EURO.minorValue && change.getCount(Bill.TWO_HUNDRED_EURO) > 0 -> {
                    resultChange.add(Bill.TWO_HUNDRED_EURO, 1)
                    change.remove(Bill.TWO_HUNDRED_EURO, 1)
                    cashBack -= Bill.TWO_HUNDRED_EURO.minorValue
                }
                cashBack > Bill.ONE_HUNDRED_EURO.minorValue && change.getCount(Bill.ONE_HUNDRED_EURO) > 0 -> {
                    resultChange.add(Bill.ONE_HUNDRED_EURO, 1)
                    change.remove(Bill.ONE_HUNDRED_EURO, 1)
                    cashBack -= Bill.ONE_HUNDRED_EURO.minorValue
                }
                cashBack > Bill.FIFTY_EURO.minorValue && change.getCount(Bill.FIFTY_EURO) > 0 -> {
                    resultChange.add(Bill.FIFTY_EURO, 1)
                    change.remove(Bill.FIFTY_EURO, 1)
                    cashBack -= Bill.FIFTY_EURO.minorValue
                }
                cashBack > Bill.TWENTY_EURO.minorValue && change.getCount(Bill.TWENTY_EURO) > 0 -> {
                    resultChange.add(Bill.TWENTY_EURO, 1)
                    change.remove(Bill.TWENTY_EURO, 1)
                    cashBack -= Bill.TWENTY_EURO.minorValue
                }
                cashBack > Bill.TEN_EURO.minorValue && change.getCount(Bill.TEN_EURO) > 0 -> {
                    resultChange.add(Bill.TEN_EURO, 1)
                    change.remove(Bill.TEN_EURO, 1)
                    cashBack -= Bill.TEN_EURO.minorValue
                }
                cashBack > Bill.FIVE_EURO.minorValue && change.getCount(Bill.FIVE_EURO) > 0 -> {
                    resultChange.add(Bill.FIVE_EURO, 1)
                    change.remove(Bill.FIVE_EURO, 1)
                    cashBack -= Bill.FIVE_EURO.minorValue
                }
                cashBack > Coin.TWO_EURO.minorValue && change.getCount(Coin.TWO_EURO) > 0 -> {
                    resultChange.add(Coin.TWO_EURO, 1)
                    change.remove(Coin.TWO_EURO, 1)
                    cashBack -= Coin.TWO_EURO.minorValue
                }
                cashBack > Coin.ONE_EURO.minorValue && change.getCount(Coin.ONE_EURO) > 0 -> {
                    resultChange.add(Coin.ONE_EURO, 1)
                    change.remove(Coin.ONE_EURO, 1)
                    cashBack -= Coin.ONE_EURO.minorValue
                }

                //Cents
                cashBack > .50 && change.getCount(Coin.FIFTY_CENT) > 0 -> {
                    resultChange.add(Coin.FIFTY_CENT, 1)
                    change.remove(Coin.FIFTY_CENT, 1)
                    cashBack -= Coin.FIFTY_CENT.minorValue
                }
                cashBack > .20 && change.getCount(Coin.TWENTY_CENT) > 0 -> {
                    resultChange.add(Coin.TWENTY_CENT, 1)
                    change.remove(Coin.TWENTY_CENT, 1)
                    cashBack -= Coin.TWENTY_CENT.minorValue
                }
                cashBack > .10 && change.getCount(Coin.TEN_CENT) > 0 -> {
                    resultChange.add(Coin.TEN_CENT, 1)
                    change.remove(Coin.TEN_CENT, 1)
                    cashBack -= Coin.TEN_CENT.minorValue
                }
                cashBack > .05 && change.getCount(Coin.FIVE_CENT) > 0 -> {
                    resultChange.add(Coin.FIVE_CENT, 1)
                    change.remove(Coin.FIVE_CENT, 1)
                    cashBack -= Coin.FIVE_CENT.minorValue
                }
                cashBack > .02 && change.getCount(Coin.TWO_CENT) > 0 -> {
                    resultChange.add(Coin.TWO_CENT, 1)
                    change.remove(Coin.TWO_CENT, 1)
                    cashBack -= Coin.TWO_CENT.minorValue
                }
                cashBack > .01 && change.getCount(Coin.ONE_CENT) > 0 -> {
                    resultChange.add(Coin.ONE_CENT, 1)
                    change.remove(Coin.ONE_CENT, 1)
                    cashBack -= Coin.ONE_CENT.minorValue
                }
                //When right combination of change are not present
                else -> throw TransactionException("Insufficient Change!")
            }
        }

        return resultChange
    }

    class TransactionException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
