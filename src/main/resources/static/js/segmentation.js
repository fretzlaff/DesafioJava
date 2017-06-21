
function changeOptions(attributeSelect) {
	var ruleSelectName = attributeSelect.id.substring(0, attributeSelect.id.indexOf('.') + 1) + 'comparisonRule';
	var ruleSelect = document.getElementById(ruleSelectName);

	removeOptions(ruleSelect);
	if (attributeSelect.value == 'AGE') {
		addIntegerOptions(ruleSelect);
	} else {
		addStringOptions(ruleSelect);
	}
}

function removeOptions(selectbox) {
	while (selectbox.options.length) {
		selectbox.remove(0);
	}
}

function addIntegerOptions(selectbox) {
	var option = document.createElement("option");
	option.value = 'EQUALS';
	option.text = 'Igual a';
	selectbox.appendChild(option);

	option = document.createElement("option");
	option.value = 'LESS_THAN';
	option.text = 'Menor que';
	selectbox.appendChild(option);

	option = document.createElement("option");
	option.value = 'LESS_IGUALS_THAN';
	option.text = 'Menor ou igual a';
	selectbox.appendChild(option);

	option = document.createElement("option");
	option.value = 'GREATHER_THAN';
	option.text = 'Maior que';
	selectbox.appendChild(option);

	option = document.createElement("option");
	option.value = 'GREATHER_EQUALS_THAN';
	option.text = 'Maior ou igual a';
	selectbox.appendChild(option);

}

function addStringOptions(selectbox) {
	var option = document.createElement("option");
	option.value = 'EQUALS';
	option.text = 'Igual a';
	selectbox.appendChild(option);

	option = document.createElement("option");
	option.value = 'CONTAINS';
	option.text = 'Contém';
	selectbox.appendChild(option);

	option = document.createElement("option");
	option.value = 'STARTS_WITH';
	option.text = 'Começa com';
	selectbox.appendChild(option);

	option = document.createElement("option");
	option.value = 'ENDS_WITH';
	option.text = 'Termina com';
	selectbox.appendChild(option);
}
