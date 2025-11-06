
console.log("login.js cargado");

function desactivar_boton(){
	
	const form = document.querySelector('#loginForm');	
	
	form.addEventListener('submit', (e) => {
		e.preventDefault();
		const error = document.querySelector('#error');
		if (error != null) {
			console.log("Error: ", error);
		}
		
	});
	
	
	
}


document.addEventListener('DOMContentLoaded', () => {
	desactivar_boton();
});