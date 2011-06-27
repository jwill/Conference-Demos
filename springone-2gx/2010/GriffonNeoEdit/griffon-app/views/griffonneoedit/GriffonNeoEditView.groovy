package griffonneoedit

application(title: 'GriffonNeoeEdit',
  size: [800,600],
  //pack: true,
  //location: [50,50],
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]) {
    widget(id: 'editPane', new neoe.ne.EditPanel(""))
}
