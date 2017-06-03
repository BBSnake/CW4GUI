package com.snayk.cw4.view

import com.snayk.cw4.logic.apriori
import com.snayk.cw4.logic.associationRules
import com.snayk.cw4.system.Database
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Alert
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.util.converter.NumberStringConverter
import tornadofx.*
import java.io.File

class MainView : View("Apriori") {
    var systemFileTextField: TextField by singleAssign()
    var chosenFile: File? = null
    var database: Database? = null
    var databaseText: TextArea by singleAssign()
    var variablesBox: VBox by singleAssign()
    var frequencyThreshold = SimpleIntegerProperty(2)
    var ruleQualityThreshold = SimpleDoubleProperty(1.0/3.0)
    override val root = vbox {
        padding = insets(10)
        spacing = 10.0

        label("File:")
        hbox {
            spacing = 5.0
            systemFileTextField = textfield {
                style {
                    prefWidth = 400.px
                }
                isEditable = false
            }
            button("Select") {
                action {
                    chosenFile = chooseFile("Choose file with system",
                            arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt"),
                                    FileChooser.ExtensionFilter("All types", "*.*"))).firstOrNull()

                    if (chosenFile?.absolutePath != null) {
                        systemFileTextField.text = chosenFile?.absolutePath
                    }
                }
            }
        }
        button("Read file") {
            action {
                if (chosenFile != null) {
                    try {
                        database = Database(chosenFile!!.readText())
                        databaseText.text = database.toString()
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "Error", "Provided file is in wrong format or contains and error!")
                    }
                } else {
                    alert(Alert.AlertType.ERROR, "Error", "No file chosen!")
                }
            }
        }
        label("Database:")
        hbox {
            spacing = 40.0
            databaseText = textarea {
                isEditable = false

            }
            variablesBox = vbox {
//                isVisible = false
                spacing = 20.0
                hbox {
                    spacing = 18.0
                    label("Frequency threshold:")
                    textfield(frequencyThreshold, NumberStringConverter()) {
                        maxWidth = 50.0
                        textProperty().addListener { _, _, newValue ->
                            if(!newValue.matches(Regex("\\d*")))
                                this.text = newValue.replace(Regex("\\D"), "")
                        }
                    }
                }
                hbox {
                    spacing = 10.0
                    label("Rule quality threshold:")
                    textfield(ruleQualityThreshold, NumberStringConverter()) {
                        maxWidth = 50.0
                    }
                }
            }
        }
        button("Generate") {
            action {
                val fList = apriori(database!!, frequencyThreshold.value)
                val rules = associationRules(database!!, fList, ruleQualityThreshold.value)
                RulesView(ruleQualityThreshold.value, rules).openWindow()
            }
        }
    }
}