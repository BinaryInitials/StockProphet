function search() {
var input, filter, table, tr, td1, i;
input = document.getElementById("filter-search");
filter = input.value.toUpperCase().split(" ");
table = document.getElementById("myTable");
tr = table.getElementsByTagName("tr");
for (i = 0; i < tr.length; i++) {
td1 = tr[i].getElementsByTagName("td")[1];
if (td1) {
var exist = false;
for(j=0;j<filter.length;j++){
if (td1.innerHTML.toUpperCase().indexOf(">"+filter[j]+"<") > -1 || filter[j] == "") {
exist = true;
}
}
if(exist){
tr[i].style.display = "";
} else {
tr[i].style.display = "none"
}
}
}
}