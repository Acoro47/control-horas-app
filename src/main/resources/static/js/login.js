
console.log("login.js cargado");

function desactivar_boton(){
	
	const form = document.querySelector('#loginForm');	
	const btnLogin = document.querySelector('#loginBtn');
	const btnLoading = document.querySelector('#loginBtnDisabled');
	const overlay = document.querySelector('#submitOverlay');
	const message = document.querySelector('#message');
	const error = document.querySelector('#error')
	
	
		
	
	form.addEventListener('submit', (e) => {
		console.log("Has clicado el botón de entrar");
		
		console.log("Mensaje: ", message);
		console.log("Error: ", error);
		
		if (btnLogin.classList.contains('d-none')) {
			e.preventDefault();
			return;
		}
	});
	
	
	
}


document.addEventListener('DOMContentLoaded', () => {
	desactivar_boton();
});