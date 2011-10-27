import com.bleedingwolf.ratpack.*
import jwill.deckchair.*
import org.json.*

class Todo {
	String id
	String text
	String dateCreated = new Date()
}

class TodoApp {
	def	app = Ratpack.app {
				set 'public', 'public'
				set 'templateRoot', 'templates'
				
				def derby = new Deckchair([name:'posts',adaptor:'derby'])
				
				get("/") {
					render 'index.html'
				}
				
				get("/todo/list") {
					def list = derby.all()
					return new JSONArray(list).toString()
				}

				post("/todo/save") {
					println params
					println "urlparams" + urlparams
					def todo = new Todo(params)
					derby.save(todo.properties, {obj ->
						println obj
						println "Finished saving."
						new JSONObject([success:true, key:obj.key]).toString()
					})
				}
				
				get("/todo/delete/:id") {
					"" + derby.remove(urlparams.id) 
				}
				

		}
		
		public TodoApp() {
			RatpackServlet.serve(app)	
		}
		
		public static void main(String[] args) {
			new TodoApp()
		}
}
