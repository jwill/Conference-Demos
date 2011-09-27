var drawTriangle = (function() {
var camera, scene, renderer;

function init() {
	// scene size
	var HEIGHT = 400
	, WIDTH = 400;
	
	// camera properties
	var FOV = 45
	, ASPECT = WIDTH/HEIGHT
	, NEAR = 0.1
	, FAR = 10000;
	
	// create a renderer, camera, and scene
	renderer = new THREE.WebGLRenderer();
	renderer.setSize (WIDTH, HEIGHT);
	camera = new THREE.Camera(FOV, ASPECT, NEAR, FAR);
	camera.position.z = 25;
	//camera.position.y = 30;
	
	scene = new THREE.Scene();
	drawTriangle();
	
	// attach the scene to the DOM
	$("#shader-demo").get(0).appendChild(renderer.domElement);
}

function drawTriangle() {
	var geometry, geoMaterial;
	
	geoMaterial = new THREE.MeshLambertMaterial({
			color:0xFF00FF
	});
	shaderMaterial = new THREE.MeshShaderMaterial({
			vertexShader: $('#geom-vertexShader').get(0).innerHTML,
			fragmentShader: $('#geom-fragmentShader').get(0).innerHTML,
			vertexColors: true
	});
	
	
	geometry = new THREE.Geometry();
	geometry.vertices.push(new THREE.Vertex(new THREE.Vector3(0, 10, 0)));
	geometry.vertices.push(new THREE.Vertex(new THREE.Vector3(-10, -10, 0)));
	geometry.vertices.push(new THREE.Vertex(new THREE.Vector3(10, -10, 0)));
	geometry.faces.push(new THREE.Face3(0,1,2));
	var triangle = new THREE.Mesh(geometry, shaderMaterial);
		
	plane = new THREE.Mesh( new THREE.Plane( 200, 200 ), new THREE.MeshBasicMaterial( { color: 0xe0e0e0 } ) );
	//plane.rotation.x = - 90 * ( Math.PI / 180 );
	plane.overdraw = true;
	scene.addObject( plane );
	
	scene.addChild(triangle);
}

function animate() {
	requestAnimationFrame (animate);
	render();
}

function render() {
	renderer.render(scene, camera);
}

init();
animate();
})();
