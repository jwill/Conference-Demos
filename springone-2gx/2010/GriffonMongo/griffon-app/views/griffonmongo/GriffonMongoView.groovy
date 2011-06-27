package griffonmongo
import java.awt.Color
import javax.swing.SwingConstants
import org.jdesktop.swingx.painter.*
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.border.DropShadowBorder
import static com.explodingpixels.macwidgets.MacIcons.*

private makeTitlePainter() {
	def comp = jxcompoundPainter() {
		jxmattePainter(fillPaint:Color.BLUE)
		jxglossPainter(paint:new Color(1.0f,1.0f,1.0f,0.2f), position:GlossPainter.GlossPosition.TOP)
	}
	return comp
}

build(AddConxView)

application(title: 'GriffonMongo',
  size: [640,480],
  //pack: true,
  layout: new MigLayout(),
  //location: [50,50],
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]) {
    // add content here
    jxtitledPanel(title:'Settings', border:new DropShadowBorder(Color.BLACK,15), constraints:'w 30%, h 100%')  {
		jxtaskPaneContainer() {
			jxtaskPane(id:'conxPane', title:'Connections', layout:new MigLayout(), animated:true) {
				scrollPane() {
					panel(id:'conxIcons', layout: new MigLayout())
				}
				button(text:'Add', constraints:'newline, cell 0 1 ', actionPerformed: {view.conx.setVisible(true)})
				button(text:'Remove', constraints:'cell 0 1', actionPerformed:{controller.removeConnection()})
			}
			jxtaskPane(id:'dbPane', title:'Databases', layout:new MigLayout(), animated:true, expanded:false) {
				scrollPane() {
					panel(id:'dbIcons', layout: new MigLayout())
				}
				//button(text:'Create', constraints:'newline, cell 0 1 ', actionPerformed: {view.makeDB.setVisible(true)})
				//button(text:'Drop', constraints:'cell 0 1', actionPerformed:{controller.dropDatabase()})
			}
			jxtaskPane(id:'collPane', title:'Collections', layout:new MigLayout(), animated:true, expanded:false) {
				scrollPane() {
					panel(id:'collIcons', layout: new MigLayout())
				}
				//button(text:'Create', constraints:'newline, cell 0 1 ', actionPerformed: {view.makeColl.setVisible(true)})
				//button(text:'Drop', constraints:'cell 0 1', actionPerformed:{controller.dropCollection()})
			}
		}
    }
    jxtitledPanel(title:'Query Results', border:new DropShadowBorder(Color.BLACK,15), constraints:'w 70%, h 100%') {
        panel(layout:new MigLayout()) {
	        hbox(constraints:'newline') {
				textArea(id:'commandField', columns:40, rows:2)
				button(text:'Run', actionPerformed:{controller.runScript(commandField.text)})
	        }
	        scrollPane(constraints:'newline, w 100%, h 100%') {
				textArea(id:'results', text:bind{model.result}, editable:false)
	        }      
        }  
    }
}
