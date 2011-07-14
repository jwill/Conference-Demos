import groovy.beans.Bindable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ScriptingDemoModel {
	@Bindable canvas = new SimpleCanvas()
	@Bindable m = new ScriptEngineManager()
	@Bindable groovyEngine = m.getEngineByName("groovy")
	@Bindable frame
}