function searchIndustry(industry) {
var table, tr, td;
table = document.getElementById("myTable");
tr = table.getElementsByTagName("tr");
for (i = 0; i < tr.length; i++) {
td = tr[i].getElementsByTagName("td")[5];
if (td) {
var s = td.innerHTML.toUpperCase().replace(/<[^>]+>/g,"");
if(s === industry.toUpperCase()){
	tr[i].style.display = "";
} else {
	tr[i].style.display = "none"
}
}
}
}