application {
    title = 'GriffonNeoeEdit'
    startupGroups = ['GriffonNeoEdit']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "GriffonNeoEdit"
    'GriffonNeoEdit' {
        model = 'griffonneoedit.GriffonNeoEditModel'
        controller = 'griffonneoedit.GriffonNeoEditController'
        view = 'griffonneoedit.GriffonNeoEditView'
    }

}
