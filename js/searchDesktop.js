function search() {
var input, filter, table, tr, td1, td2, td3, td4, i;
input = document.getElementById("filter-search");
filter = input.value.toUpperCase().split(" ");
table = document.getElementById("myTable");
tr = table.getElementsByTagName("tr");
for (i = 0; i < tr.length; i++) {
td1 = tr[i].getElementsByTagName("td")[2];
td2 = tr[i].getElementsByTagName("td")[3];
td3 = tr[i].getElementsByTagName("td")[4];
td4 = tr[i].getElementsByTagName("td")[5];
if (td1 && td2 && td3 && td4) {
var exist = false;
for(j=0;j<filter.length;j++){
if (td1.innerHTML.toUpperCase().indexOf(">"+filter[j]+"<") > -1 || td2.innerHTML.toUpperCase().indexOf(">"+filter[j]+"<") > -1 || td3.innerHTML.toUpperCase().indexOf(">"+input.value.toUpperCase() +"<") > -1 || td4.innerHTML.toUpperCase().indexOf(">"+input.value.toUpperCase() +"<") > -1 || filter[j] == "") {
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
