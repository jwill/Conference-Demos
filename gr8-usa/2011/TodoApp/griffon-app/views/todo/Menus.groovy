menuBar {
	menu('File') {
		menuItem('Print to PDF', actionPerformed:{ controller.createPDF()})
		menuItem('Exit', actionPerformed:{System.exit(0)})
	}
}
