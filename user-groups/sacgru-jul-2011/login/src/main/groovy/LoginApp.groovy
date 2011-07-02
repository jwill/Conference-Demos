import com.bleedingwolf.ratpack.*

class LoginApp {
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
		}
		Auth.doAuth(app)
		RatpackServlet.serve(app)
	}
}
