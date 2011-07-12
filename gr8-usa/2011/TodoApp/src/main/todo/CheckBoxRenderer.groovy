package todo
import javax.swing.*
import java.awt.Component
import javax.swing.table.*
import org.json.JSONObject
import org.jdesktop.swingx.JXDatePicker


class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground())
			setBackground(table.getSelectionBackground())
		} else {
			setForeground(table.getForeground())
			setBackground(table.getBackground())
		}
		
		if (value != null)
			setSelected(value)
		return this
	}
}

class DatePickerEditor extends AbstractCellEditor implements TableCellEditor {
	def component = new JXDatePicker()
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		component.setDate(value)
		return component
	}
	
	public Object getCellEditorValue() {
		return component.getDate()
	}
}
