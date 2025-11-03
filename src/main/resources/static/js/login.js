/**
 * 
 */

document.addEventListener('DOMContentLoaded', function () {
  const form = document.querySelector('form[method="post"]');
  const btn = document.getElementById('loginBtn');
  const btnText = document.getElementById('loginBtnText');
  const btnSpinner = document.getElementById('loginBtnSpinner');
  const inputs = form.querySelectorAll('input, button');

  form.addEventListener('submit', function (e) {
    // Evitar doble envío si ya estamos en proceso
    if (btn.disabled) {
      e.preventDefault();
      return;
    }

    // Si la validación nativa falla, no bloquear (dejar que el navegador muestre el error)
    if (!form.checkValidity()) {
      // allow browser to show validation messages; do not disable
      return;
    }

    // Deshabilitar todos los controles del formulario
    inputs.forEach(el => el.disabled = true);

    // Cambiar texto y mostrar spinner
    btnText.textContent = 'Entrando...';
    btnSpinner.classList.remove('d-none');
    btn.setAttribute('aria-busy', 'true');
    btn.disabled = true;

    // Si el envío es interrumpido por JS más abajo, re-habilitará en el catch o manejo de errores
    // No preventDefault aquí: dejamos que el formulario se envíe al servidor.
  });

  // Caso en que quieras re-habilitar automáticamente si ocurre un error JS (opcional)
  // window.addEventListener('error', () => restore());
  // window.addEventListener('unhandledrejection', () => restore());

  function restore() {
    inputs.forEach(el => el.disabled = false);
    btnText.textContent = 'Entrar';
    btnSpinner.classList.add('d-none');
    btn.removeAttribute('aria-busy');
    btn.disabled = false;
  }
});
