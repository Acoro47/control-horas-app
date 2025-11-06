
console.log("login.js cargado");

function desactivar_boton(){
	
	const form = document.querySelector('#loginForm');	
	const btnLogin = document.querySelector('#loginBtn');
	const btnLoading = document.querySelector('#loginBtnDisabled');
	const overlay = document.querySelector('#submitOverlay');
	
	
		
	
	form.addEventListener('submit', (e) => {


		const messageEl = document.querySelector('#message');
	    const errorEl = document.querySelector('#error');
	    const message = messageEl ? messageEl.textContent.trim() : null;
	    const error = errorEl ? errorEl.innerHTML.trim() : null;

	    console.log("Has clicado el botón de entrar");
	    console.log("Mensaje:", message);
	    console.log("Error:", error);
		
		if (btnLogin.classList.contains('d-none')) {
			console.log("Formulario enviado")
			return;
		}
	});
	
	
	
}


document.addEventListener('DOMContentLoaded', () => {
	desactivar_boton();
});