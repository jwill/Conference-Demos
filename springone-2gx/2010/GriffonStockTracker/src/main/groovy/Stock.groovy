import ca.odell.glazedlists.gui.*

class Stock {
	String name, symbol
	def change, open, high, low, volume, recentValue
	
	public static createStockObject(Object[] values) {
		def stock = new Stock()
		stock.symbol = values[0]
		stock.name = values[2]
		stock.change = values[3]
		stock.open = values[4]
		stock.high = values[5]
		stock.low = values[6]
		stock.volume = values[7]
		stock.recentValue = values[1]
		return stock
	}
}

class StockTableFormat implements TableFormat<Stock> {
	@Override
	public int getColumnCount() {
		return 3
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column) {
			case 0:
				return "Symbol"
				break
			case 1:
				return "Value"
				break
			case 2:
				return "Change"
				break
		}
	}
	
	@Override
	public Object getColumnValue(Stock stock, int column) {
		switch (column) {
			case 0:
				stock.symbol
				break
			case 1:
				'$' + stock.recentValue
				break
			case 2:
				stock.change
				break
		}
	}
}
