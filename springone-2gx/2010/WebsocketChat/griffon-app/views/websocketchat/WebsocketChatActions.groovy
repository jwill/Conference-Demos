package websocketchat


actions {
	action(id:'setEndpointAction', title:"Set Endpoint", closure:{})
	action(id:'loginAction', closure:{})
	action(id:'logoutAction', closure:{})
	action(id:'exitAction', text:"Exit",  closure:{System.exit(0)})
}
