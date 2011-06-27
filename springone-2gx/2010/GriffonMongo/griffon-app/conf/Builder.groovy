root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }
    'griffon.app.ApplicationBuilder' {
        view = '*'
    }
}
root.'TangoiconsGriffonAddon'.addon=true

root.'MiglayoutGriffonAddon'.addon=true


root.'griffon.builder.trident.TridentBuilder'.view = '*'


jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}

root.'griffon.builder.macwidgets.MacWidgetsBuilder'.view = '*'

