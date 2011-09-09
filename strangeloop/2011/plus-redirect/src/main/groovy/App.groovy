import com.bleedingwolf.ratpack.*

class App {
		def app = Ratpack.app {
				set 'public', 'public'
				set 'templateRoot', 'templates'
				
				get("/") {
					println request.getServerName()
					if (request.getServerName() == "jameswilliams.be")
						response.sendRedirect("/blog/entry/index")
					""
				}
				
				get("/\\+") {
					response.sendRedirect("http://profiles.google.com/James.L.Williams/posts")
					""
				}
				
			
		}

		public App() {
		//	RatpackServlet.serve(app)	
		}
		
		public static void main(String[] args) {
			new App()
		}
}
