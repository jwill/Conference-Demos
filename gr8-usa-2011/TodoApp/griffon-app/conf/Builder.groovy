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




jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}

root.'I18nGriffonAddon'.addon=true

root.'ValidationGriffonAddon'.addon=true


