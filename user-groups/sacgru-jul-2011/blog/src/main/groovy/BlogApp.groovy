import com.bleedingwolf.ratpack.*

class BlogApp {
	public static void main(String []args) {
		def app = Ratpack.app {
				set 'public', 'public'
				set 'templateRoot', 'templates'
				
				get("/") {
					render 'index.html'
				}
				
				post("/login") {
					def result = doAuth(request)
					if (result != "Unauthorized") {
						"Logged in with username ${result[0]} and password ${result[1]}"
					} else {
						"Unauthorized"
					}
				}
				
				get("/post/list") {
					"Calling list from blogapp file"
				}
		}
		AuthHelper.doAuth(app)
		CrudHelper.doCrud(app, 'post', ['/post/list'])
		RatpackServlet.serve(app)
	}
}
