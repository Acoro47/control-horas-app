/**
 * 
 */

document.addEventListener('DOMContentLoaded', () => {
	const form  = document.querySelector('form[method="post]');
	if (!form) return;
	
	const btn = document.getElementById('loginBtn');
	const btnLoader = document.getElementById('loginBtnDisabled');
	const overlay = document.getElementById('submitOverlay');
	const controls = Array.from(form.querySelectorAll('input, select, textarea, button'));
	
	if (!btn || !btnLoader) {
		form.addEventListener('submit', (e) => {
			const submitBtn = form.querySelector('[type="submit"]');
			if (submitBtn) {
				submitBtn.disabled = true;
			}
		});
	}
	
	form.addEventListener('submit', (e) => {
		if (!form.checkValidity()) return;
		
		if (btn.disabled) {
			e.preventDefault();
			return;
		}
		
		controls.forEach(el => { 
			try { el.disabled = true; 
			
			} catch(_){
				
			}
		 });
		 
		 btn.classList.remove('d-none');
		 btn.setAttribute('aria-hidden', 'false');
		 
		 btnLoader.classList.remove('d-none');
		 btnLoader.setAttribute('aria-hidden', 'false');
		 btnLoader.setAttribute('aria-busy', 'true');
		 
		 if (overlay) {
			overlay.classList.remove('d-none');
			overlay.setAttribute('aria-hidden', 'false');
		 }
	});
	
	window.addEventListener('pageshow', () => {
		if (!btn.classList.contains('d-none') && btn.disabled) {
			restore();
		}
		
		if (!btnLoader.classList.contains('d-none') && btnLoader.disabled) {
					restore();
		}
	});
	
	function restore() {
		controls.forEach(el => {
			try {
				el.disabled = false;
			} catch (_) {
				
			}
		});
		btn.classList.remove('d-none');
		btn.removeAttribute('aria-hidden');
		btn.disabled = false;
		
		btnLoader.classList.remove('d-none');
		btnLoader.removeAttribute('aria-hidden');
		btnLoader.removeAttribute('aria-busy');
		
		if (overlay) {
			overlay.classList.add('d-none');
			overlay.setAttribute('aria-hidden','true');
		}
	}
	
	window.loginFormRestore = restore;
});
