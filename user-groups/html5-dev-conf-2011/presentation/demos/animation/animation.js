var animateZombie = (function() {
var   camera,
			projector,
			scene,
			renderer,
			mesh,
			animOffset = 0,
			walking = false,
			stats;
		function initialize() {
		/*	camera = new THREE.QuakeCamera( {
				fov: 60,
				aspect: window.innerWidth / window.innerHeight,
				near: 1,
				far: 20000,
				movementSpeed: 100,
				lookSpeed: 0.01,
				noFly: true,
				lookVertical: true
			});*/
			camera = new THREE.Camera( 60, window.innerWidth / window.innerHeight, 1, 10000 );
			//camera.position.y = 130;
			//camera.position.x = -100;
			camera.position.z = 10;
			camera.target.position.y = 5; 

			scene = new THREE.Scene();
			// Add Troll
			var loader = new THREE.JSONLoader();
			loader.load( { model: "demos/animation/zombie.js", callback: createScene } );

			// Create renderer
			renderer = new THREE.WebGLRenderer( { antialias: true } );
			renderer.setSize( 640, 480 );

			$("#animation-demo").get(0).appendChild(renderer.domElement);

			console.log("in asssnim");
		}

		function createScene( geometry ) {
console.log("loaded");
		console.log(geometry.materials);
		//	for (var i=0; i<geometry.materials.length; i++) {
				geometry.materials[0][0].shading = THREE.FlatShading;
				geometry.materials[0][0].morphTargets = true;	
				geometry.materials[1][0].shading = THREE.FlatShading;
				geometry.materials[1][0].morphTargets = true;
				geometry.materials[2][0].shading = THREE.FlatShading;
				geometry.materials[2][0].morphTargets = true;
				geometry.materials[3][0].shading = THREE.FlatShading;
				geometry.materials[3][0].morphTargets = true;
				geometry.materials[4][0].shading = THREE.FlatShading;
				geometry.materials[4][0].morphTargets = true;
				geometry.materials[5][0].shading = THREE.FlatShading;
				geometry.materials[5][0].morphTargets = true;
		//	}
			

			var material = new THREE.MeshFaceMaterial();

			mesh = new THREE.Mesh( geometry, material);
			mesh.scale = new THREE.Vector3(5, 5, 5);

			plane = new THREE.Mesh( new THREE.Plane( 200, 200 ), new THREE.MeshBasicMaterial( { color: 0xe0e0e0 } ) );
	plane.position.z = -10;
	plane.overdraw = true;
	scene.addObject( plane );
			
	var light = new THREE.DirectionalLight( 0xefefff, 2 );
				light.position.x = 1;
				light.position.y = 1;
				light.position.z = 1;
				light.position.normalize();
				scene.addLight( light );

				var light = new THREE.DirectionalLight( 0xffefef, 2 );
				light.position.x = - 1;
				light.position.y = - 1;
				light.position.z = - 1;
				light.position.normalize();
				scene.addLight( light );
	
			scene.addObject( mesh );
		}

		function animate() {
			requestAnimationFrame( animate );

			render();
		}
		
		var 
			duration = 6000,
			keyframes = 36,
			interpolation = duration / keyframes,
			lastKeyframe = 0, currentKeyframe = 0;

		function render() {

			if ( mesh ) {

				// Alternate morph targets

				var time = new Date().getTime() % duration;

				var keyframe = Math.floor( time / interpolation ) + animOffset;

				if ( keyframe != currentKeyframe ) {

					mesh.morphTargetInfluences[ lastKeyframe ] = 0;
					mesh.morphTargetInfluences[ currentKeyframe ] = 1;
					mesh.morphTargetInfluences[ keyframe ] = 0;

					lastKeyframe = currentKeyframe;
					currentKeyframe = keyframe;

				}
//console.log(keyframe);
				mesh.morphTargetInfluences[ keyframe ] = ( time % interpolation ) / interpolation;
				mesh.morphTargetInfluences[ lastKeyframe ] = 1 - mesh.morphTargetInfluences[ keyframe ];
                //console.log('current: ' + mesh.morphTargetInfluences[ keyframe ]);
			}

			renderer.render( scene, camera );
		}
		initialize();
		animate();
});
