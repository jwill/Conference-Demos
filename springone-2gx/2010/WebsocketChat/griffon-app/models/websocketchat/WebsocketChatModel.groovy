package websocketchat
import org.jwebsocket.api.*
import groovy.beans.Bindable
import org.jwebsocket.client.token.*

import ca.odell.glazedlists.*
import ca.odell.glazedlists.swing.*

class WebsocketChatModel {
   // @Bindable String propName
   def client
   @Bindable String username = System.getProperty("user.name")
   def endpoint = "ws://localhost:5555/slides"
   //def endpoint = "184.106.138.53"
   EventList msgEventList = new BasicEventList()
   EventListModel msgModel = new EventListModel(msgEventList)
}
