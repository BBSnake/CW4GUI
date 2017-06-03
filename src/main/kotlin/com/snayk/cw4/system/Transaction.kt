package com.snayk.cw4.system

data class Transaction(val itemsList: List<String>) {
    override fun toString(): String {
        var transaction = ""
        itemsList.forEach { transaction += "$it " }
        return transaction
    }
}

