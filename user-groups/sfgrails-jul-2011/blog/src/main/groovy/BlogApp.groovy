import com.bleedingwolf.ratpack.*
import jwill.deckchair.*
import org.json.*

class BlogApp {
	def	app = Ratpack.app {
				set 'public', 'public'
				set 'templateRoot', 'templates'
				
				def derby = new Deckchair([name:'posts',adaptor:'derby'])
				
				get("/") {
					def list = derby.all()
					render '/entry/index.html', [posts: list.reverse(), contextPath:request.getContextPath()]
				}
				
				get("/entry/list") {
					def list = derby.all()
					render '/entry/index.html', [posts: list, contextPath:request.getContextPath()]
				}

				post("/entry/save") {
					def entry = new Entry(params)
					derby.save(entry.properties, {obj ->
						println "Finished saving."
						new JSONObject([success:true]).toString()
					})
				}

				get("/entry/create") {
					render '/entry/create.html', [contextPath:request.getContextPath()]
				}

				get("/entry/show/:id") {
					def entry = derby.get(urlparams.id)
					render '/entry/show.html', [entry: entry, contextPath:request.getContextPath()]
				}
				
				get("/entry/delete/:id") {
					derby.remove(urlparams.id)
				}
				
				get("/archive") {
					
				}
				
				get("/search") {
					
				}

		}
		public BlogApp() {
			AuthHelper.doAuth(app)
			RatpackServlet.serve(app)	
		}
		
		public static void main(String[] args) {
			new BlogApp()
		}
}
