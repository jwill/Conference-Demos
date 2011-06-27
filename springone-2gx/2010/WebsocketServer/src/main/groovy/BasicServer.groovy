import org.json.*
import websocket4j.server.WebSocket
import websocket4j.server.WebServerSocket

public class SlideServer extends Thread {
	static ArrayList <WebSocket> sockets = []
	static WebSocket admin
	static String adminToken
	String clientToken = ''
	static questions = []
	long currentSlide = 1
	WebSocket ws
	
	public SlideServer(WebSocket ws) {
		this.ws = ws
		sockets.add(ws)
	}
	
	def processMessage = { msg ->
		cleanupConnections()
		// convert to JSON object
		def outer = new JSONObject(msg)
		def json = new JSONObject(outer.getString("data"))
		println msg
		def op = json.getString('operation')
		switch(op) {
			case 'login':
				processLogin(json)
				break
			case 'changeSlide':
				processChangeSlide(json)
				break
			case 'question':
				processQuestion(json)
				break
			case 'getinfo':
				processGetInfo(json)
				break
			case 'broadcast':
				processBroadcast(json)
				break
			
		}
	}
	
	def processLogin = { json ->
		def user = json.getString("user")
		def pwd = json.getString("pwd")
		if ((user.equals('jwill')) && pwd.equals('12345')) {
			admin = ws
			adminToken = UUID.randomUUID()
			
			def obj = new JSONObject()
			obj.put('operation', 'loginSuccess')
			obj.put('token', adminToken)
			
			admin.sendMessage(obj.toString())
		} else {
			
		}
	}
	
	def cleanupConnections = {
		def newList = []
		sockets.each { 
			if (!it.isClosed()) {
				newList.add(it)
			}
		}
		println "cleaning up connections"
		sockets = newList
	}
	
	def processBroadcast = { json ->
		println "broadcast"
		println sockets
		sockets.each {println it.isClosed() }
		for (s in sockets) {
			s.sendMessage(json.toString())
		}
	//	admin?.sendMessage(json.getString("message"))
    }
    
    def processChangeSlide = { json ->
		//if (json.getString('token').equals(adminToken)) {
			def obj = new JSONObject()
			obj.put('operation', 'changeSlide')
			obj.put('newSlide', json.getLong('newSlide'))
			obj.put ('clientToken', clientToken)
			obj.put('url', 'file:///home/jwill/Desktop/d/img0.png')
			
			println "change slide"
			for (s in sockets) {
				s.sendMessage(obj.toString())
			}
			currentSlide = json.getLong('newSlide')
		//}
		
    }
    
    def processGetInfo = {
		if (json.getString('token').equals(adminToken)) {
			def obj = new JSONObject()
			obj.put('slide', currentSlide)
			obj.put('url', '')
			
			admin.sendMessage(obj.toString())
			
		}
    }
    
    def processQuestion = { json ->
		//if (json.getString('token')?.equals(adminToken)) {
			questions.add(json.getString("message"))
			println "question"
			println json.toString()
			for (s in sockets) {
				s.sendMessage(json.toString())
			}
			admin?.sendMessage(json.toString())
		//}
		
    }
	
	public void handleConnection() {
        try {
            while (true) {
                String message = ws.getMessage();
                processMessage(message)
              //  ws.sendMessage(message);
                System.out.println("Received: " + message);
                if (message.equals("exit"))
                    break;
            }
        } catch (IOException e) {}
        finally {
            try {
				ws.close();
            } catch (IOException e) { }
        }
    }

    public void run() {
        handleConnection();
    }
}
