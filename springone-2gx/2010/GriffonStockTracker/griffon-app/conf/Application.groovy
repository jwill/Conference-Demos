application {
    title = 'GriffonStockTracker'
    startupGroups = ['GriffonStockTracker']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "GriffonStockTracker"
    'GriffonStockTracker' {
        model = 'griffonstocktracker.GriffonStockTrackerModel'
        view = 'griffonstocktracker.GriffonStockTrackerView'
        controller = 'griffonstocktracker.GriffonStockTrackerController'
        
    }

}
