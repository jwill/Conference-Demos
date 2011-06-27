package griffonstocktracker

import org.jdesktop.http.*
import ca.odell.glazedlists.*
import ca.odell.glazedlists.swing.*
import au.com.bytecode.opencsv.CSVReader
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionListener

class GriffonStockTrackerController {
    // these will be injected by Griffon
    def model
    def view

    void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
        model.stockEventList.getReadWriteLock().writeLock().lock();
		try {
	        ['AAPL', 'GOOG', 'EBAY', 'MSFT', 'AMZN', 'ORCL', 'IBM'].each {
				def stock = new Stock(symbol:it)
				model.stockEventList.add(stock)
	        }
        } finally {
			model.stockEventList.getReadWriteLock().writeLock().unlock();
        }
        
        
        retrieveStockInfo()
        view.symbolsTable.with {
			getSelectionModel().addListSelectionListener(listener)
			getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
			setAutoCreateRowSorter(false)
			setRowSorter(null)
        }
    }
    
    def removeSymbol = {
		def stock = model.selectionModel.getSelected()[0]
		try{
			model.stockEventList.remove(stock)
		} catch (Exception ex) {}
    }
    
    def addSymbol =  {
		def stock = new Stock(symbol: view.stock.text)
		def found = model.stockEventList.find{it.symbol.equals(stock.symbol)}
		if (!found) {
			model.stockEventList.add(stock)
			retrieveStockInfo()
		}
		edt {
			view.stock.text = ''
		}
    }
    
    def listener = [valueChanged:{evt ->
		try {
			def stock = model.selectionModel.getSelected()[0]
			edt {
				view.with {
					txtName.text = stock.name
					txtValue.text = stock.recentValue
					txtChange.text = stock.change
					txtOpen.text = stock.open
					txtHigh.text = stock.high
					txtLow.text = stock.low
					txtVolume.text = stock.volume
					
					if (stock.change.startsWith('+')) {
						txtChange.setForeground(java.awt.Color.GREEN)
					} else if (stock.change.startsWith('-')) {
						txtChange.setForeground(java.awt.Color.RED)
					}
				}
		}
		} catch(Exception ex) {}
    }] as ListSelectionListener
    
    def retrieveStockInfo = {
		def session = new Session()
		def request = new Request(model.root.replace("STOCKS", model.stockEventList.collectAll({it.symbol}).join('+')))
		
		doOutside {
			def response = session.execute(request)
			if (response.getStatusCode().getCode() == 200) {
			    def body = response.getBody()
			    def file = File.createTempFile("stocks","csv")
			    file.write(body)
			    CSVReader reader = new CSVReader(new FileReader(file.getCanonicalPath()));
			    List entries = reader.readAll();

			    def eventList = new BasicEventList<Stock>()
			    // convert to stock
			    entries.each {
					def stock = Stock.createStockObject(it)
					def isContained = false
					for (int i=0; i<model.stockEventList.size(); i++) {
						def obj = model.stockEventList.get(i)
						if (obj.symbol.equals(stock.symbol)) {
							model.stockEventList.set(i, stock)
							isContained = true
						}
					}
					if (!isContained)
						model.stockEventList.add(stock)
			    }
			    file.delete()
			}
			view.statusLabel.text = "Retrieved from Yahoo! Finance at " + new Date() 
		}
    }
}
