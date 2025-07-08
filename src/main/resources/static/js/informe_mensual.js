

function calcularResumenMensual() {
	console.log("Cargando...")
  	const horasContrato = parseFloat(document.getElementById('horasContratoInput').value || 0);
  	const tarifaExtra = parseFloat(document.getElementById('tarifaExtraInput').value || 0);
  	const minutosContrato = horasContrato * 60;
	
  	let totalMinutos = 0;
  	let totalExtrasMinutos = 0;
  	let totalEuros = 0;
	  
  	const resumenTotales = document.getElementById('resumenTotales');
	
  	document.querySelectorAll('#tablaResumen tbody tr').forEach(row => {
    const minutosDia = parseInt(row.dataset.minutos || 0);
    totalMinutos += minutosDia;
		
	console.log("Fila -> Minutos:", row.dataset.minutos);
	
    // Duración total
    const hTotal = Math.floor(minutosDia / 60);
    const mTotal = minutosDia % 60;
    row.querySelector('.total').textContent = `${hTotal}:${String(mTotal).padStart(2,'0')} h`;
	
    // Horas extra
    const minExtra = Math.max(0, minutosDia - minutosContrato);
    totalExtrasMinutos += minExtra;
    const hExtra = Math.floor(minExtra / 60);
    const mExtra = minExtra % 60;
    row.querySelector('.extra').textContent = `${hExtra}:${String(mExtra).padStart(2,'0')} h`;
	
    // Importe por horas extra
    const euros = (minExtra / 60) * tarifaExtra;
    totalEuros += euros;
    row.querySelector('.importe').textContent = `${euros.toFixed(2)} €`;
	
    // Mostrar horas por contrato
    row.querySelector('.contrato').textContent = `${horasContrato.toFixed(2)} h`;
  });
  
  	console.log("Elemento resumenTotales:", resumenTotales);
  
  	console.log("Total minutos:", totalMinutos);
  	console.log("Total horas extra:", totalExtrasMinutos);
  	console.log("Total importe:", totalEuros.toFixed(2));

  	// 🧾 Mostrar totales si tienes elementos para ello
  	if (resumenTotales) {
    	resumenTotales.innerHTML = `
      	<p><strong>Total horas trabajadas:</strong> ${Math.floor(totalMinutos / 60)}:${String(totalMinutos % 60).padStart(2,'0')} h</p>
      	<p><strong>Total horas extra:</strong> ${Math.floor(totalExtrasMinutos / 60)}:${String(totalExtrasMinutos % 60).padStart(2,'0')} h</p>
      	<p><strong>Importe total:</strong> ${totalEuros.toFixed(2)} €</p>
    	`;
  	}
}

document.addEventListener('DOMContentLoaded', () => {
  	calcularResumenMensual();
  	document.getElementById('horasContratoInput').addEventListener('input', calcularResumenMensual);
  	document.getElementById('tarifaExtraInput').addEventListener('input', calcularResumenMensual);
});
