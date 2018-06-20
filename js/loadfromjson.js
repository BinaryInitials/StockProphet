function loadFromJSON(suffix){
	var container = document.getElementById("content");
	container.innerHTML = "";
	var request = new XMLHttpRequest();
	request.open('GET', 'stocks_mobile_'+suffix+'.json');
	request.onload = function() {
	if (request.status >= 200 && request.status < 400) {
	  var data = JSON.parse(request.responseText);
	  var htmlString = "";
	  for (i = 0; i < data.length; i++) {
		  htmlString += data[i].row;
	  }
	  container.insertAdjacentHTML('beforeend', htmlString);
	} else {
	  console.log("We connected to the server, but it returned an error.");
	}

	};

	request.onerror = function() {
	console.log("Connection error");
	};
	request.send();
}