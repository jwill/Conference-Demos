package griffonstocktracker
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.decorator.HighlighterFactory

application(title: 'GriffonStockTracker',
  size: [425,350],
  layout: new MigLayout(),
  //location: [50,50],
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]) {
	panel (layout:new MigLayout()){
	scrollPane() {
		table(id:'symbolsTable', constraints:'w 160px', model:model.stockModel, selectionModel:model.selectionModel)
	}
	panel(layout:new MigLayout(), constraints:'w !, h 100%') {
		label('Name:')
		label(id:'txtName')
		label('Value:', constraints:'newline')
		label(id:'txtValue')
		label('Change:', constraints:'newline')
		label(id:'txtChange')
		label('Open:', constraints:'newline')
		label(id:'txtOpen')
		label('High:', constraints:'newline')
		label(id:'txtHigh')
		label('Low:', constraints:'newline')
		label(id:'txtLow')
		label('Volume:', constraints:'newline')
		label(id:'txtVolume')
	}
	}
    
    panel(layout:new MigLayout(), constraints:'newline') {
		label("Symbol")
		textField(id:"stock", columns:10)
		button(icon:silkIcon(icon:'add'), actionPerformed:controller.addSymbol)
		button(icon:silkIcon(icon:'delete'), actionPerformed:controller.removeSymbol)
    }
    jxstatusBar(){
		label(id: 'statusLabel', 'test')
    }
}

