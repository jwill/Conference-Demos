application {
    title = 'GriffonMongo'
    startupGroups = ['GriffonMongo']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    frameClass = 'org.jdesktop.swingx.JXFrame'
}
mvcGroups {
    // MVC Group for "GriffonMongo"
    'GriffonMongo' {
        view = 'griffonmongo.GriffonMongoView'
        model = 'griffonmongo.GriffonMongoModel'
        controller = 'griffonmongo.GriffonMongoController'
        
    }

}
