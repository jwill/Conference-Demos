root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }
    'griffon.app.ApplicationBuilder' {
        view = '*'
    }
}
root.'MiglayoutGriffonAddon'.addon=true

root.'griffon.builder.macwidgets.MacWidgetsBuilder'.view = '*'

jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}

root.'GlazedlistsGriffonAddon'.addon=true
