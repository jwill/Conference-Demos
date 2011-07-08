class CrudHelper {
	static doCrud = {app, domainName, excludes ->
			def getMethods = [
				('/'+domainName+'/list'):{ -> "List" + '\n'+ response.toString()},
				('/'+domainName+'/edit/:id'):{ -> "Edit"}
				('/'+domainName+'/show/:id'):{-> "Show Item ${urlparams.id}"},
			]
			
			def postMethods = [
				('/'+domainName+'/create'):{->},
				('/'+domainName+'/save'):{->},
				('/'+domainName+'/update/:id'):{->}
			]
			
			getMethods.each {
				if (!excludes.contains(it.key))
					app.get(it.key, it.value)
			}
			
			postMethods.each {
				if (!excludes.contains(it.key))
					app.post(it.key, it.value)
			}
  }
}
