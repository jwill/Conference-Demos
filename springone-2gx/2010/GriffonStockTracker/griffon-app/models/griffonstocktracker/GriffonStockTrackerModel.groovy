package griffonstocktracker

import groovy.beans.Bindable
import ca.odell.glazedlists.*
import ca.odell.glazedlists.swing.*



class GriffonStockTrackerModel {
   // @Bindable String propName
   def root = 'http://finance.yahoo.com/d/quotes.csv?s=STOCKS&f=sl1nc1ohgv'
   
   EventList stockEventList = new BasicEventList<Stock>()
   EventTableModel stockModel = new EventTableModel(stockEventList, new StockTableFormat())
   EventSelectionModel selectionModel = new EventSelectionModel(stockEventList)
}
