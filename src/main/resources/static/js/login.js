
console.log("login.js cargado");

function desactivar_boton(){
	
	const form = document.querySelector('#loginForm');	
	const btnLogin = document.querySelector('#loginBtn');
	const btnLoading = document.querySelector('#loginBtnDisabled');
	const overlay = document.querySelector('#submitOverlay');
	const message = document.querySelector('#message');
	const error = document.querySelector('#error')
	
	form.addEventListener('submit', (e) => {
		if (btnLogin.classList.contains('d-none')) {
			e.preventDefault();
			return;
		}
	});
	
	console.log(message);
	console.log(error);
	
	
}


document.addEventListener('DOMContentLoaded', () => {
	desactivar_boton();
});