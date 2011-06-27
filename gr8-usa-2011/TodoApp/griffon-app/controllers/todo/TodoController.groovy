package todo
import javax.swing.*
import javax.xml.parsers.*
import org.jdesktop.xswingx.*
import org.xhtmlrenderer.pdf.*
import groovy.xml.MarkupBuilder
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener

class TodoController {
    // these will be injected by Griffon
    def model
    def view

     void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
    	
    		doLater {
    			load()
    		}
    		// setup renderers and editors
    		view.table.setDefaultRenderer(Boolean.class, new CheckBoxRenderer())
    		view.table.setDefaultEditor(Date.class, new DatePickerEditor())
    		view.table.setDefaultEditor(Boolean.class, new DefaultCellEditor(new JCheckBox()))
    		
    		// setup prompts
    		PromptSupport.setPrompt("Add Todo, type here", view.todoField)
    		PromptSupport.setPrompt('Enter tags comma separated', view.tagsField)
    		
    		// table model listener
    		model.todoTableModel.addTableModelListener([tableChanged:{evt ->
					def i = evt.getFirstRow()
					def j = evt.getLastRow()
					if (i == j && evt.getType() == TableModelEvent.UPDATE) {
						def changedTodo = model.todos.get(i)
						model.derby.save(changedTodo.toMap(), {obj -> println obj; println "saved todo"})
					}
    		}] as TableModelListener)
     }

    // void mvcGroupDestroy() {
    //    // this method is called when the group is destroyed
    // }
	
    def addItem = {
    	if (model.validate()) {
    		def todo = new TodoItem(dueDate:model.dueDate, todoText:model.todoText, tags:model.tagsText.split(','))
    		try {
    			model.derby.save(todo.toMap(), {obj -> todo.id = obj.key
    			model.todos.add(todo)
    			})
    			// clear view widgets
    			view.todoField.text = ''
    			view.tagsField.text = ''
    			view.datePicker.setDate(null)
    		} catch(Exception ex) { }
    		
    		
    	} else {
    		model.errors.each{error ->
    			println error
    		}
    	}
    }
    
    def load = {
    	doOutside {
    		def todos = model.derby.all()
    		todos.each{
    			def todo = TodoItem.fromMap(it)
    			model.todos.add(todo)
    		}
    	}
    }
    
    def deleteCompleted = {
    	def lock = model.todos.getReadWriteLock().writeLock()
    	lock.lock()
    	def list = model.todos.findAll{item ->
    		item.isDone == true
    	}
    	list.each { item ->
    		model.derby.remove(item.id)
    		model.todos.remove(item)
    	}
    	lock.unlock()
    }
    
    def writer
    
    def createHTML = {
    	writer = new StringWriter()
			def builder = new MarkupBuilder(writer)
			
			builder.html {
				head{
						title('My Todo List')
				}
				body {
					h2("My Todo List")
					br {}
					table (width:'90%', border:1, 'border-spacing':0){
						tr {
								td('Completed')
								td('Description')
								td('Due Date')
								td('Tags')
						}
						model.todos.each { item ->
								tr {
									if (item.isDone) {
										td('Done')
									} else {
										td()
									}
									
									td(item.todoText)
									td(item.dueDate)
									td(item.tagsToString())
								}
						}
					}
				}
			}
    }
    
    def createPDF = {
    	createHTML()
    	def file = File.createTempFile("todos","txt")
    	file.write(writer.toString())
    	def docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    	def doc = docBuilder.parse(file)
    	
    	def renderer = new ITextRenderer()
    	renderer.setDocument(doc, null)
    	
    	def outputFile = "todos.pdf"
    	def os = new FileOutputStream(outputFile)
    	renderer.layout()
    	renderer.createPDF(os)
    	os.close()
    	
    	edt {
    		JOptionPane.showMessageDialog(null, "PDF created.")
    	}
    }
}
