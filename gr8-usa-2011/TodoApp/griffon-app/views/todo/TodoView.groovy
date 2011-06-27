package todo

import net.miginfocom.swing.MigLayout
import net.sourceforge.gvalidation.swing.ErrorMessagePanel

application(title: 'TodoApp',
  size: [640,480],
  locationByPlatform:true, 
  layout: new MigLayout()) {
  	build(Menus)
  	
  	// Add Todo field
  	jxtextField(id:'todoField', columns:45, text:bind(target:model, 'todoText'), constraints:'w 90%', errorRenderer:'for: todoText, styles: [highlight, popup]')
  	button(id:'btnAdd', text:'Add', constraints:'w 10%, wrap', actionPerformed:{controller.addItem()})
  	// More options (date, tags, etc)
  	jxtaskPane(title:'More Options', constraints: 'w 100%, spanx 2, wrap', layout:new MigLayout()) {
  		label(text:'Due Date:', constraints:'wrap')
  		jxdatePicker(id:'datePicker', date:bind(target:model,'dueDate'), constraints:'wrap')
  		label(text:'Tags', constraints:'wrap')
  		jxtextField(id:'tagsField', columns:40, text:bind(target:model, 'tagsText'))
  	}
  	// Tasks
  	jxtitledPanel(title:'Tasks') {
  		scrollPane() {
  			//jxlist(listData:['A','B','C'] as String[])
  			jxtable(id:'table', model:model.todoTableModel)
  		}
  	}
  	button('Remove Completed Tasks', constraints:'newline', actionPerformed:{controller.deleteCompleted()})
}
