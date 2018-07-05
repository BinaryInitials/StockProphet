function dropDown() {
    document.getElementById("myDropDown").classList.toggle("show");
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      openDropdown.style.display = "";
    }
}

window.onclick = function(event) {
    if ($(event.target).attr("onclick") && $(event.target).attr("onclick").toString().match('.*loadFromJSON.*') !== null) {
	    var dropdowns = document.getElementsByClassName("dropdown-content");
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
    		var openDropdown = dropdowns[i];
			openDropdown.style.display = "none";
	    }
	}
}