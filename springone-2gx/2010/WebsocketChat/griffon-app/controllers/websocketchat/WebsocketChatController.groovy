package websocketchat
import org.json.*
import javax.swing.JOptionPane
import org.jwebsocket.api.*
import org.jwebsocket.client.token.*
import websocket4j.client.*

class WebsocketChatController {
    // these will be injected by Griffon
    def model
    def view

    void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
    }
    
    def openConnection = {
		model.client = new BaseTokenClient()
		try {
			model.client.open(model.endpoint)
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Connection failed.", "alert", JOptionPane.ERROR_MESSAGE);
		}
		def listener = [
			processToken:{aEvent, aToken ->
				println "token"+aToken
				def json = new JSONObject(aToken.toString())
				switch(json.getString("operation")) {
					case "broadcast":
						def sender = json.getString("user")
						def msg = json.getString("message")
						println sender
						println model.username
						println !sender.equals(model.username)
						if (!sender.equals(model.username)) 
							model.msgEventList.add("$sender: $msg")
					break
					case "question":
						def sender = json.getString("user")
						def msg = json.getString("message")
						if (!sender.equals(model.username)) 
							model.msgEventList.add("$sender asked a question: $msg")
					break
					case "changeSlide":
						processChangeSlide(json)
					break
				}
			}, processOpened:{aEvent -> println aEvent}, 
			processPacket:{evt, pkt -> 
		}] as WebSocketClientTokenListener
 		
		model.client.addTokenClientListener(listener)
    }
    
    def processChangeSlide = { json ->
		//if (!json.getString('clientToken').equals(null)) {
			def url = json.getString("url")
			println url
			view.screenshot.clear()
			def pic = view.label(icon:view.imageIcon(new URL(url)))
			view.screenshot.add(pic)
			view.screenshot.revalidate() 
		//}
    }
    
    def login = {
		//if (!model.client)
			openConnection()
			
		//def username = System.getProperty("user.name")
		//doOutside {
			//model.client.login(username, '')
		//}
		//model.msgEventList.add("Logging in...")
    }
    
    def setEndpoint = { url ->
		model.endpoint = url
    }
    
    def setUsername = { name ->
		model.username = name
    }
    
    def closeConnection = {
		doOutside {
			model.client.close()
		}
    }
    
    def send = { message ->
		if(view.cbox.getSelectedItem().equals('Send Question')) {
			sendQuestion(message)
		} else {
			sendMessage(message)
		}
    }
    
    def sendMessage = { message ->
		def json = new JSONObject()
		json.put("message", message)
		json.put("user", model.username)
		json.put("operation", "broadcast")
		
		try {
			doOutside {
				try{
					model.client.broadcastText(json.toString())
					model.msgEventList.add("Me: "+message)
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Not connected. Please login", "alert", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		
		} catch (Exception ex) {
			println "Broken pipe"
			JOptionPane.showMessageDialog(null, "Not connected. Please login", "alert", JOptionPane.ERROR_MESSAGE);
		}
		
    }
    
    def sendQuestion = { question ->
		def json = new JSONObject()
		json.put("message", question)
		json.put("user", model.username)
		json.put("operation", "question")
		
		doOutside {
			model.client?.broadcastText(json.toString())
		}
		model.msgEventList.add("I asked a question: "+question)
    }
}
