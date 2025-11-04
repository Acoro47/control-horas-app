/**
 * 
 */

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');
  if (!form) return;

  const loginBtn = document.getElementById('loginBtn');
  const loginBtnDisabled = document.getElementById('loginBtnDisabled');
  const overlay = document.getElementById('submitOverlay');
  const errorBox = document.getElementById('loginError');
  const controls = Array.from(form.querySelectorAll('input, select, textarea, button'));

  function setLoading(on) {
    controls.forEach(el => { try { el.disabled = on; } catch (_) {} });
    if (on) {
      if (loginBtn) loginBtn.classList.add('d-none');
      if (loginBtnDisabled) loginBtnDisabled.classList.remove('d-none');
      if (overlay) { overlay.classList.remove('d-none'); overlay.setAttribute('aria-hidden','false'); }
    } else {
      if (loginBtn) loginBtn.classList.remove('d-none');
      if (loginBtnDisabled) loginBtnDisabled.classList.add('d-none');
      if (overlay) { overlay.classList.add('d-none'); overlay.setAttribute('aria-hidden','true'); }
    }
  }

  function showError(text) {
    if (!errorBox) return;
    errorBox.textContent = text;
    errorBox.classList.remove('d-none');
  }

  form.addEventListener('submit', async (e) => {
    if (!form.checkValidity()) return;
    e.preventDefault();
    if (loginBtn && loginBtn.disabled) return;

    setLoading(true);
    if (errorBox) { errorBox.classList.add('d-none'); errorBox.textContent = ''; }

    try {
      const action = form.getAttribute('action') || window.location.href;
      const method = (form.getAttribute('method') || 'GET').toUpperCase();
      const formData = new FormData(form);

      const response = await fetch(action, {
        method,
        body: formData,
        credentials: 'same-origin',
        headers: { 'X-Requested-With': 'XMLHttpRequest' },
        redirect: 'manual'
      });

      if (response.status >= 300 && response.status < 400) {
        const location = response.headers.get('Location');
        if (location) { window.location.href = location; return; }
        window.location.reload(); return;
      }

      let payload = null;
      const ct = response.headers.get('content-type') || '';
      if (ct.includes('application/json')) {
        try { payload = await response.json(); } catch (_) { payload = null; }
      }

      if (!response.ok) {
        const msg = (payload && (payload.error || payload.message || payload.msg)) || `Error ${response.status}`;
        showError(msg);
        setLoading(false);
        return;
      }

      if (payload) {
        if (payload.redirect) { window.location.href = payload.redirect; return; }
        if (payload.success) { window.location.reload(); return; }
      }

      window.location.reload();
    } catch (err) {
      console.error('Login request failed', err);
      showError('Error de red. Inténtalo de nuevo.');
      setLoading(false);
    }
  });

  window.addEventListener('pageshow', (event) => {
    if (event.persisted) setLoading(false);
  });

  window.loginFormRestore = () => setLoading(false);
});