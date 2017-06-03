package com.snayk.cw4.view

import tornadofx.*

class RulesView(val ruleQualityThreshold: Double, val rules: String) : View("Association Rules") {
    override val root = vbox {
        padding = insets(10)
        spacing = 10.0
        textarea {
            text = "Rule Quality Threshold: support * trust >= $ruleQualityThreshold\n\n$rules"
            isEditable = false
        }
    }
}
