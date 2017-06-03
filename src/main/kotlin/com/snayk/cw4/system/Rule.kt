package com.snayk.cw4.system

class Rule(val predecessors: List<String>,
           val successor: String,
           var support: Double = 0.0,
           var trust: Double = 0.0) {

    override fun toString(): String {
        var rule = ""
        for(i in 0 until predecessors.size - 1)
            rule += "${predecessors[i]} ^ "
        rule += "${predecessors.last()} => $successor support * trust = ${support*trust}"
        return rule
    }
}

