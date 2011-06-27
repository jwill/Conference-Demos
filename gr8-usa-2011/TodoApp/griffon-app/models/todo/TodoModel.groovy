package todo
import jwill.deckchair.*

import groovy.beans.Bindable
import ca.odell.glazedlists.*
import ca.odell.glazedlists.swing.*
import net.sourceforge.gvalidation.annotation.Validatable

@Validatable
class TodoModel {
	@Bindable String todoText
	@Bindable Date dueDate
	@Bindable tagsText
	BasicEventList<TodoItem> todos = new BasicEventList<TodoItem>()
	def todoTableModel = new EventTableModel<TodoItem>(todos, new TodoTableFormat())
	def derby = new Deckchair([name:'todos', adaptor:'derby'])
	// to swallow events from initial load
	def isLoading = true
	
	static constraints = {
		todoText(blank:false)
		dueDate(nullable:true)
		tagsText(blank:true)
	}
}