class GriffonTalkWebsocketsGriffonPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9 > *' 
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = '<UNKNOWN>'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    // TODO Fill in these fields
    def author = 'James Williams'
    def authorEmail = 'james.l.williams@gmail.com'
    def title = 'Griffon-Talk Websocket Plugin'
    def description = '''
Adds websocket support to control Griffon-Talk presentations.
'''

    // URL to the plugin's documentation
    def documentation = '' //'http://griffon.codehaus.org/GriffonTalkWebsockets+Plugin'
}
