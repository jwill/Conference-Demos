application {
    title = 'Todo'
    startupGroups = ['Todo']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "Todo"
    'Todo' {
        model      = 'todo.TodoModel'
        view       = 'todo.TodoView'
        controller = 'todo.TodoController'
    }

}
