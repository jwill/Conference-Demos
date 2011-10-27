	views = [
			{view: 'Label', anchor: 'left', rect:'0 10 50 35', text:'Todo Text:'}
			{view: 'TextField',rect:'60 10 150 35', placeholder:'Please enter text', id:'itemText'}
			{view: 'Button', id:'addItem', rect: "220 10 90 35", text:"Add Item"}
			{view: 'Button', id:'removeItem', rect:'310 10 90 35', text:'Remove Selected Item'} 
			{ view: 'List', rect: '0 50 400 270', id: 'list' }
		]

		box = uki ( {
			view: 'Box'
			rect: '0 0 1000 1000'
			childViews: views
		})
		
		box.attachTo document.getElementById("layout")
		
		# Actions
		
		uki('#addItem').click( ()->
			text = uki('#itemText').value()
			uki('#list').addRow(0, text)
			uki('#itemText').attr('value', "")

			data = {text: text}
			s = (data) -> 
				console.log data
			$.post(
				'/todo/save', data, s,'json'
			)
		)
		
		uki('#removeItem').click () ->
			row = uki('#list').selectedIndex()
			text = uki('#list').selectedRow()

			data = {text:text}
					
			uki('#list').removeRow row if row isnt null
			$.get( '/todo/delete', data)
			
		
		
		# Load items
		parseData = (items) ->
			for item in items
				uki('#list').addRow(0, item.text) 
		
		$.getJSON('/todo/list', parseData)
