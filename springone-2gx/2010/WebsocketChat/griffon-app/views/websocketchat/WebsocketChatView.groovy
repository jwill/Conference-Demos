package websocketchat
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.decorator.HighlighterFactory

application(title: 'WebsocketChat',
  size: [1000,500],
//  resizable:false,
  //location: [50,50],
  //pack:true,
  locationByPlatform:true,
  layout:new MigLayout(),
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]) {
    // add content here
	menuBar {
		menu('File') {
			menuItem(text:'Set Endpoint', actionPerformed:{})
			menuItem(text:'Login', actionPerformed:controller.login)
			menuItem(text:'Logout', actionPerformed:{})
			menuItem(text:"Exit",  actionPerformed:{System.exit(0)})
		}
	}
	panel(id:'screenshot', constraints:'w 700px , h 100%')
	panel (layout:new MigLayout(), constraints:'w !, h 100%') {
		scrollPane(id:'scroll', constraints:'h 90%, w !, wrap'){
			jxlist(constraints:'w !', model:model.msgModel, highlighters:[HighlighterFactory.createSimpleStriping()])
		}
		panel (layout:new MigLayout(), constraints:'w !'){
				comboBox(id:'cbox', items:['Send Message', 'Send Question'])
				label('as')
				textField(columns:10, text:bind(source:model, sourceProperty:'username', mutual:true), constraints:'wrap')
				textArea(id:'msgText', columns:15, lineWrap:true, constraints:'wrap')
				button(text:'Send', actionPerformed:{controller.send(msgText?.text)})
		}
	}
}
