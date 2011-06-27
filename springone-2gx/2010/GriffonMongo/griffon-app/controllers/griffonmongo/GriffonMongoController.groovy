package griffonmongo
import MongoUtils
import org.json.*
import com.mongodb.*
import com.gmongo.GMongo
import javax.swing.SwingConstants
import org.jdesktop.swingx.painter.*
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.border.DropShadowBorder
class GriffonMongoController {
    // these will be injected by Griffon
    def model
    def view

    void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
        loadJSONProps()
        // setup button groups
    }
    
    def loadJSONProps = { ->
		def f = new File("mongo.json")
		if (!f.exists()) {
			f.createNewFile()
		//	println f.properties
			model.jsonProps = new JSONObject()
		} else {
		//	println f.properties
			model.jsonProps = new JSONObject(f.getText())
			// populate connections
			populateConnections(model.jsonProps.getJSONArray("conx"))
		}
    }
    
    def populateConnections = { array ->
		view.conxIcons.clear()
		view.dbIcons.clear()
		view.collIcons.clear()
		for (int i = 0; i<array.length(); i++) {
			def obj = array.get(i);
			println obj
			// create object
			model.connections.put(obj.getString("name"), [host:obj.getString("host"), port:obj?.getInt("port")])
			// create view code
			edt {
				createConxWidget(obj.getString("name"))
			}
		}
    }
    
    def createConxWidget = { name ->
		def btn = view.toggleButton(text:name, id:name + 'Conx',
				verticalTextPosition:SwingConstants.BOTTOM, 
				horizontalTextPosition:SwingConstants.CENTER, actionCommand:name,
				icon:new javax.swing.ImageIcon(getClass().getResource('/computer.png')), actionPerformed:{openConx(name)})
		model.conxBtnGroup.add(btn)
		view.conxIcons.add(btn)
    }
    
    def createDBIcon = { name ->
		def btn = view.toggleButton(text:name, id:name + 'DB',
				verticalTextPosition:SwingConstants.BOTTOM, 
				horizontalTextPosition:SwingConstants.CENTER, 
				icon:new javax.swing.ImageIcon(getClass().getResource('/db.png')), actionPerformed:{openDB(name)})
		model.dbBtnGroup.add(btn)
		view.dbIcons.add(btn)
    }
    
    def createCollIcon = { name ->
		def btn = view.toggleButton(text:name, id:name + 'Coll',
				verticalTextPosition:SwingConstants.BOTTOM, 
				horizontalTextPosition:SwingConstants.CENTER, 
				icon:new javax.swing.ImageIcon(getClass().getResource('/tar.png')))
		model.collBtnGroup.add(btn)
		view.collIcons.add(btn)
    }
    
    def openConx = { name ->
		def conxInfo = model.connections.get(name)
		if (conxInfo.port != 0) {
			model.mongo = new Mongo(conxInfo.host, conxInfo.port)
		} else model.mongo = new Mongo(conxInfo.host)
		populateDBs()
    }
    
    def populateDBs = {
		view.dbPane.setExpanded(false)
		def dbNames = model.mongo.getDatabaseNames()
		view.dbIcons.clear()
		view.collIcons.clear()
		model.databases = [:]
		model.collections = [:]
		dbNames.each { n ->
			model.databases.put(n, model.mongo.getDB(n))
			createDBIcon(n)
		}
		view.dbIcons.updateUI()
		view.dbPane.setExpanded(true)
    }
    
    def openDB = { name ->
		// db already open, get the collections
		def db = model.databases.get(name)
		populateCollections(db)
    }
    
    def populateCollections = { db ->
		view.collPane.setExpanded(false)
		def collNames = db?.getCollectionNames()
		view.collIcons.clear()
		model.collections = [:]
		collNames.each { n ->
			model.collections.put(n, db.getCollection(n))
			createCollIcon(n)
		}
		view.collIcons.updateUI()
		view.collPane.setExpanded(true)
    }
    
    def addConnection = { name, host, port ->
		// create entry for item
		// push to json array and save
		def obj = new JSONObject()
		obj.put("name", name)
		obj.put("host", host)
		obj.put("port",port)
		
		def array = model.jsonProps.getJSONArray("conx")
		array.put([name:name, host:host, port:port])
		model.jsonProps.put("conx", array)
		
		// Save file
		def f = new File("mongo.json")
		f.write(model.jsonProps.toString())
		loadJSONProps()
		
		view.with {
			txtName.text = ''
			txtHost.text = ''
			txtPort.text = ''
			conx.setVisible(false)
		}
		view.conxIcons.updateUI()
    }
    
    // library doesn't seem to implement remove on JSONArray
    // removing by copying
    def removeConnection = { 
		def btn = model.conxBtnGroup.getSelection()
		def array2 = new JSONArray()
		def array = model.jsonProps.getJSONArray("conx")
		for (int i = 0; i<array.length(); i++) {
			def obj = array.get(i);
			println obj
			if ( obj.getString("name").equals(btn.actionCommand) ) {
				// do nothing
			} else {
				array2.put(obj)
			}
		}
		model.jsonProps.put("conx", array2)
		
		// Save file
		def f = new File("mongo.json")
		f.write(model.jsonProps.toString())
		
		loadJSONProps()
		view.conxIcons.updateUI()
    }
    
    def createDatabase = { dbName ->
		model.mongo.getDB(dbName)
		populateDatabases()
    }
    
    def createCollection = { collName ->
    
    }
    
    def dropDatabase = { dbName ->
    
    }
    
    def dropCollection = { collName ->
    
    }
    
    def runScript = { script ->
		def binding = new Binding()
		model.databases.keySet().each {
			binding.setVariable(it, model.databases.get(it))
		}
		model.collections.keySet().each {
			def coll = model.collections.get(it)
			MongoUtils.decorateCollection(coll)
			binding.setVariable(it, coll)
		} 
		def os = new ByteArrayOutputStream()
		def stream = new PrintStream(os)
		System.setOut(stream)
		
		binding.setVariable("mongo",model.mongo)
		
      doOutside {
         try {
            def result = new GroovyShell(binding).evaluate(script)
            model.result = script+ '\n' + os.toString() + '\n' + result
         } catch(x) {x.printStackTrace()}
      }
   }
}
