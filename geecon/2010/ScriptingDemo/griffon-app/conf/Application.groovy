application {
    title='ScriptingDemo'
    startupGroups = ['ScriptingDemo']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "ScriptingDemo"
    'ScriptingDemo' {
        model = 'ScriptingDemoModel'
        controller = 'ScriptingDemoController'
        view = 'ScriptingDemoView'
    }

}
