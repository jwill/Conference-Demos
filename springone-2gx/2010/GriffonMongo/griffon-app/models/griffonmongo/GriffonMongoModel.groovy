package griffonmongo

import groovy.beans.Bindable
import javax.swing.ButtonGroup

class GriffonMongoModel {
   // button groups
   def conxBtnGroup = new ButtonGroup()
   def dbBtnGroup = new ButtonGroup()
   def collBtnGroup = new ButtonGroup()
   
   @Bindable result
   def jsonProps
   def defaultPropertiesFile = "mongo.json"
   
   def mongo
   def connections = [:]
   def databases = [:]
   def collections = [:]
}
