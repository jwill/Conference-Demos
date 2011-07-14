import java.awt.BorderLayout

application(title:'ScriptingDemo',
  size:[300,300],
  resizable:false,
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
				imageIcon('/griffon-icon-32x32.png').image,
				imageIcon('/griffon-icon-16x16.png').image]
) {
		widget(model.canvas)
}

