package com.snayk.cw4.logic

import com.snayk.cw4.system.Database
import com.snayk.cw4.utils.combinations

fun apriori(db: Database, frequencyThreshold: Int): List<List<List<String>>> {
    // list of all fk..f2
    val fList: MutableList<List<List<String>>> = mutableListOf()
    val f1 = mutableListOf<String>()
    val itemsList = db.transactions.flatMap { it.itemsList }
    // get all frequent items (size == 1)
    itemsList
            .filter { item -> itemsList.count { it == item} >= frequencyThreshold && !f1.contains(item) }
            .forEach { if(!f1.contains(it)) f1.add(it) }
    f1.sort()
    // generate all frequent combinations of size == 2
    val combinations = f1.combinations(2)
    val f2 = db.frequentCombinations(combinations, frequencyThreshold)
    fList.add(f2)
    val fk: MutableList<List<String>> = mutableListOf()
    var k = 3
    do {
        fk.clear()
        val interCk: MutableSet<List<String>> = mutableSetOf()
        val ck: MutableList<List<String>> = mutableListOf()
        val consideredFk = fList[k-3]
        // join all items with the same k-1 elements
        for(item in consideredFk) {
            val same = consideredFk.filter { it.subList(0, k-2) == item.subList(0, k-2) }
            if(same.size == 1)
                continue
            val joined: MutableSet<String> = mutableSetOf()
            same.flatMap { it }.forEach { joined.add(it) }
            interCk.add(joined.toList())
        }
        // slice the items with apriori property
        interCk.forEach {
            val combs = it.combinations(k-1)
            if(combs.all { consideredFk.contains(it.reversed()) })
                ck.add(it)
            println()
        }
        // finally add all frequent items to fk
        ck.forEach {
            if(db.frequencyOfCombination(it) >= frequencyThreshold)
                fk.add(it)
        }
        k++
        fList.add(fk)
    } while (fk.size > 1)
    return fList.toList()
}

