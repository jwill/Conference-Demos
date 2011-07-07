class Post {
	String key
	String author
	String title
	String dateCreated = new Date()
	String content
	
	public Post(Map map) {
		super()
		this.title = map.title
		this.content = map.content
	}
}

class Comment {
	String key
	String postId
	String author
	String content
	String dateCreated
}
