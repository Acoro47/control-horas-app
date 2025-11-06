
console.log("login.js cargado");

function desactivar_boton(){
	
	const form = document.querySelector('#loginForm');	
	const btnLogin = document.querySelector('#loginBtn');
	
	
		
	
	form.addEventListener('submit', (e) => {
		e.preventDefault();
		console.log("Enviando el formulario...");
		console.log("BtnLogin", btnLogin);
		btnLogin.ariaHidden = true;
		console.log("BtnLogin", btnLogin);
		
	});
	
	
	
}


document.addEventListener('DOMContentLoaded', () => {
	desactivar_boton();
});