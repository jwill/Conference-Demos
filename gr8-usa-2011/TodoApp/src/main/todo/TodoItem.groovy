package todo
import org.json.JSONObject
import java.text.SimpleDateFormat

class TodoItem {
	String id
	String todoText
	Date dueDate
	List <String>tags
	Boolean isDone
	
	static SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

	
	public tagsToString() {
		Arrays.sort(tags)
		tags.join(',')
	}
	
	public toMap() {
		return [key:id,todoText:todoText, dueDate:dueDate, tags:tags, isDone:isDone]
	}
	
	public static fromMap(map) {
		println map
		def todo = new TodoItem(id:map.id, todoText: map.todoText)
		
		// handle nulls for isDone
		if (map.isDone != null && map.isDone != JSONObject.NULL) 
			todo.isDone = map.isDone
		else todo.isDone = false
		
		def list = []
		for (i in 0..<map.tags.length()){ 
			list.add( map.tags.get(i).toString() )
		}
		todo.tags = list
		if (map.dueDate != null && map.dueDate != JSONObject.NULL)
			todo.dueDate = (Date)parser.parse(map.dueDate)
		
		return todo
	}
}
