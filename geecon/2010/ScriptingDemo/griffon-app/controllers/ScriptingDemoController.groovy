import java.awt.BorderLayout
class ScriptingDemoController {
    // these will be injected by Griffon
    def model
    def view

    void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
		model.groovyEngine.put("canvas", model.canvas)
		model.canvas.engine = model.groovyEngine
		javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false)
	//	println app.properties
		model.frame = new groovy.swing.SwingBuilder().frame(size:[400,400], pack:true) {
			panel (layout: new BorderLayout()) {
					textArea(id:'scriptPanel', rows:10, columns:50, constraints:BorderLayout.NORTH)
					button(text:'Execute Script', constraints: BorderLayout.SOUTH, actionPerformed:{executeScript(scriptPanel.text)})
			}
		}.show()
    }

    
    def executeScript = { text ->
		try {
			model.canvas.script = text
		} catch (Exception ex) {
			ex.printStackTrace()
		}
		
    }
    
}