package todo
import javax.swing.JCheckBox
import javax.swing.JTextField
import org.jdesktop.swingx.JXDatePicker
import ca.odell.glazedlists.gui.AdvancedTableFormat
import ca.odell.glazedlists.gui.WritableTableFormat

public class TodoTableFormat implements AdvancedTableFormat<TodoItem>, WritableTableFormat<TodoItem> {
	public int getColumnCount() {
		return 4
	}
	
	public String getColumnName(int column) {
		if (column == 0) return "Complete"
		else if (column == 1) return "Description"
		else if (column == 2) return "Due Date"
		else if (column == 3) return "Tags"
		
		throw new IllegalStateException()
	}
	
	public Object getColumnValue(TodoItem item, int column) {
		if (column == 0) return item.isDone
		else if (column == 1) return item.todoText
		else if (column == 2) return item.dueDate
		else if (column == 3) return item.tagsToString()
		
		throw new IllegalStateException()
	}
	
	public Class getColumnClass(int column) {
		if (column == 0) return Boolean.class
		else if (column == 1) return String.class
		else if (column == 2) return Date.class
		else if (column == 3) return String.class
		
		throw new IllegalStateException()
	}
	
	public Comparator getColumnComparator(int column) {
		return null
	}
	
	public boolean isEditable(TodoItem obj, int column) {
		return true
	}
	
	public TodoItem setColumnValue(TodoItem obj, editedValue, int column) {
		if (column == 0) obj.isDone = editedValue
		else if (column == 1) obj.todoText = editedValue
		else if (column == 2) obj.dueDate = editedValue
		else if (column == 3) {
			def tagCloud = editedValue.split(',')
			obj.tags = tagCloud
		}
		return obj
	}
}
