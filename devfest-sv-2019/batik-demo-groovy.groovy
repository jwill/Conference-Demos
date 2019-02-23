@Grab(group = 'org.apache.xmlgraphics', module = 'batik-all', version = '1.10', type = 'pom')
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import groovy.swing.*

import org.apache.batik.swing.gvt.*
import org.apache.batik.swing.svg.*
import org.apache.batik.swing.JSVGCanvas;

def svgCanvas = new JSVGCanvas()
def messageLabel

def loadSVG = {evt ->
    def chooser = new JFileChooser(".")
    chooser.setFileFilter(new FileNameExtensionFilter("SVG Files", "svg"))
    int choice = chooser.showOpenDialog(null)
    if (choice == JFileChooser.APPROVE_OPTION) {
        File f = chooser.getSelectedFile();
        try {
            svgCanvas.setURI(f.toURL().toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

def frame = new SwingBuilder().frame(title: "Batik", size: [400, 400], 
    defaultCloseOperation: JFrame.EXIT_ON_CLOSE, show: true) {
        borderLayout()
        panel(constraints : BorderLayout.NORTH) {
            flowLayout(alignment: FlowLayout.LEFT)
            button(id:"loadButton", "Load", actionPerformed: loadSVG)
            messageLabel = label()
        }
        widget(svgCanvas, constraints : BorderLayout.CENTER)
}



// Listeners -- Interfaces as Anonymous Inner Class Implementation
def docListener = [
    documentLoadingStarted: { evt -> messageLabel.text = "Document Loading..."},
    documentLoadingCompleted: { evt -> messageLabel.text = "Document Loaded."},
    documentLoadingFailed: {evt -> }, documentLoadingCancelled: {evt ->}
] as SVGDocumentLoaderListener

def treeBuilderListener = [
    gvtBuildStarted: {evt -> messageLabel.text = "Build Started..."}, 
    gvtBuildFailed: {evt ->},gvtBuildCancelled: {evt ->},
    gvtBuildCompleted: {evt -> 
        messageLabel.text = "Build Done."
        frame.pack()},
] as GVTTreeBuilderListener

def treeRendererListener = [
    gvtRenderingPrepare: {evt -> messageLabel.text = "Rendering Started..."},
    gvtRenderingCompleted: {evt -> messageLabel.text = ""}
] as GVTTreeRendererListener

svgCanvas.with {
    addSVGDocumentLoaderListener(docListener)
    addGVTTreeBuilderListener(treeBuilderListener)
    addGVTTreeRendererListener(treeRendererListener)
}


//svgCanvas.addSVGDocumentLoaderListener(docListener)
//svgCanvas.addGVTTreeBuilderListener(treeBuilderListener)
//svgCanvas.addGVTTreeRendererListener(treeRendererListener)