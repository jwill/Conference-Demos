package griffonmongo

import net.miginfocom.swing.MigLayout
frame (title:'Add a Connection', id:'conx', size:[200,200], layout:new MigLayout()) {
	label(text:'Name')
	textField(id:'txtName', columns:15, constraints:'wrap')
	label(text:'Host')
	textField(id:'txtHost', columns:15, constraints:'wrap')
	label(text:'Port')
	textField(id:'txtPort', columns:4, constraints:'wrap')
	button(text:'Add', actionPerformed:{controller.addConnection(txtName.text, txtHost.text, txtPort.text)})
	button(text:'Cancel', actionPerformed:{view.conx.setVisible(false)})
}
