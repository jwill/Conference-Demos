root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }
    'griffon.app.ApplicationBuilder' {
        view = '*'
    }
}
jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}

root.'GlazedlistsGriffonAddon'.addon=true

root.'SilkiconsGriffonAddon'.addon=true

root.'MiglayoutGriffonAddon'.addon=true

root.'SwingxWsGriffonAddon'.addon=true
