package com.snayk.cw4.logic

import com.snayk.cw4.system.Database
import com.snayk.cw4.system.Rule
import com.snayk.cw4.utils.combinations

fun associationRules(db: Database, fList: List<List<List<String>>>, ruleQualityThreshold: Double): String {
    val rules: MutableList<Rule> = mutableListOf()
    for (fk in fList.reversed()) {
        for (item in fk) {
            val predecessors = item.combinations(item.size - 1)
            for (predecessor in predecessors) {
                val successor = item.filterNot { predecessor.contains(it) }
                val rule = Rule(predecessor.reversed(), successor.first())
                val support = db.ruleSupport(rule)
                val trust = db.ruleTrust(rule)
                if (support * trust >= ruleQualityThreshold) {
                    rule.support = support
                    rule.trust = trust
                    rules.add(rule)
                }
            }
        }
    }
    var rulesText = ""
    rules.forEach { rulesText += "$it\n" }
    return rulesText
}

