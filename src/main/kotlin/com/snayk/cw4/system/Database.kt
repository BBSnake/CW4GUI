package com.snayk.cw4.system

class Database(fileText: String) {
    val transactions: MutableList<Transaction> = mutableListOf()

    init {
        val rows = fileText.trim().split("\n")
        rows.map { it.trim().split(" ") }
                .mapTo(transactions) { Transaction(it) }
    }

    override fun toString(): String {
        var system = ""
        transactions.forEach { system += "$it\n" }
        return system
    }

    fun frequentCombinations(combinations: List<List<String>>, frequencyThreshold: Int): List<List<String>> {
        val f: MutableList<List<String>> = mutableListOf()
        for (comb in combinations) {
            val frequency = frequencyOfCombination(comb)
            if (frequency >= frequencyThreshold)
                f.add(comb.reversed())
        }
        return f
    }

    fun frequencyOfCombination(combination: List<String>): Int {
        var frequency = 0
        for ((itemsList) in transactions) {
            if (combination.all { itemsList.contains(it) })
                frequency++
        }
        return frequency
    }

    fun ruleSupport(rule: Rule): Double {
        var numerator = 0.0
        for((itemsList) in transactions) {
            if(rule.predecessors.all { itemsList.contains(it) } && itemsList.contains(rule.successor))
                numerator += 1.0
        }
        return numerator / transactions.size
    }

    fun ruleTrust(rule: Rule): Double {
        var numerator = 0.0
        var denominator = 0.0
        for((itemsList) in transactions) {
            if(rule.predecessors.all { itemsList.contains(it) }) {
                denominator += 1.0
                if(itemsList.contains(rule.successor))
                    numerator += 1.0
            }
        }
        return numerator / denominator
    }
}
