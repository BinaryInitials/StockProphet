function searchIndustry(industry) {
var table, tr, td;
var searchTerm = industry.replace(/(&[a-z]+; |[^A-Za-z0-9 ])/g,"").replace(/  /g," ").toUpperCase().trim();
table = document.getElementById("myTable");
tr = table.getElementsByTagName("tr");
for (i = 0; i < tr.length; i++) {
td = tr[i].getElementsByTagName("td")[5];
if (td) {
var s = td.innerHTML.replace(/<[^>]+>/g,"").replace(/&[a-z]+; /g,"").toUpperCase().trim();
if(searchTerm === s){
	tr[i].style.display = "";
} else {
	tr[i].style.display = "none"
}
}
}
}
