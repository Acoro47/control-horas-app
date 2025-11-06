
console.log("login.js cargado");

function desactivar_boton(){
	
	const form = document.querySelector('#loginForm');	
	const btnLogin = document.querySelector('#loginBtn');
	
	
		
	
	form.addEventListener('submit', (e) => {
		console.log("Enviando el formulario...");
		console.log("BtnLogin", btnLogin);
		btnLogin.ariaDisabled = true;
		console.log("BtnLogin", btnLogin);
		e.preventDefault();
	});
	
	
	
}


document.addEventListener('DOMContentLoaded', () => {
	desactivar_boton();
});