/**
 * Confirmação de excluxão de contato
 * @Eric_Martins
 * @param id
 */

function confirmar(id){
	let resposta = confirm("Confirmar a exclusão deste contato?")
	if(resposta === true){
		//alert(id)
		window.location.href = "delete?id=" + id;
	}
}