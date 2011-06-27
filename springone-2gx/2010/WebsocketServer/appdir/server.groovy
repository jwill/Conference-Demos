import websocket4j.server.WebServerSocket;
import websocket4j.server.WebSocket;
import com.bleedingwolf.ratpack.Ratpack
import com.bleedingwolf.ratpack.RatpackServlet

def app = Ratpack.app {
    
    get("/") {
        def ua = headers['user-agent']
        "Your user-agent: ${ua}"
    }
    
    get("/foo/:name") {
        "Hello, ${urlparams.name}"
    }
    
    get("/users") {
    
    }
    
    get("/questions") {
		println "questions"
		println SlideServer.questions
		def questions = SlideServer.questions
		def text = '<html><body>'
		for (int i=0; i<questions.size(); i++) {
			def q = questions[i]
			println q.class
			text += '<ul>'
			text += '<li><i>'+q+'</i></li>'
			text += '</ul>'
		
		}
		
		text += '</body></html>'
		text
    }
}

RatpackServlet.serve(app)

WebServerSocket socket = new WebServerSocket(5555);
try {
	while (true) {
		
		WebSocket ws = socket.accept();
		System.out.println("GET " + ws.getRequestUri());
		if (ws.getRequestUri().equals("/slides"))
			(new SlideServer(ws)).start()
		else {
			System.out.println("Unsupported Request-URI");
			try {
				ws.close();
			} catch (IOException e) {
			}
		}
	}

} finally {
	socket.close();
}
