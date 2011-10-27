import com.bleedingwolf.ratpack.*
import org.json.*

import com.gmongo.GMongo

class TodoApp {
	def	app = Ratpack.app {
				set 'public', 'public'
				set 'templateRoot', 'templates'
				
				def mongo = new GMongo("127.0.0.1", 27017)
				def db = mongo.getDB("gmongo")
								
				get("/") {
					render 'index.html'
				}
				
				get("/todo/list") {
					def list = db.todos.find().toArray()
					return new JSONArray(list).toString()
				}

				post("/todo/save") {
					def todo = [params]
					db.todos.insert todo
					"{true}"
				}
				
				get("/todo/delete") {
					db.todos.remove([text:params.text])
					"{true}"
				}
		}
		
		public TodoApp() {
			RatpackServlet.serve(app)	
		}
		
		public static void main(String[] args) {
			new TodoApp()
		}
}
