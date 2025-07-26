/**
 * 
 */

function calcularTotales(){
	const minContrato = 240;
	var horasTotales = 0;
	var minTotales = 0;
	var horasExtras = 0;
	var minExtras = 0;
	
	document.querySelectorAll('#tablaResumen tbody tr').forEach(row => {
		const min = parseInt(row.dataset.minutos);
		minTotales += min;
		minExtras += min - minContrato;
		
		
		
	});
	
	document.querySelector('#totalDuracion').textContent = minTotales / 60;
	document.querySelector('#totalExtra').textContent = minExtras / 60;
	
}


document.addEventListener('DOMContentLoaded', () => {
	calcularTotales();	
});
