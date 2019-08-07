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
var s1 = td1.innerHTML.toUpperCase().replace(/<[^>]+>/g,"");
var s2 = td2.innerHTML.toUpperCase().replace(/<[^>]+>/g,"");
var s3 = td3.innerHTML.toUpperCase().replace(/<[^>]+>/g,"");
var s4 = td4.innerHTML.toUpperCase().replace(/<[^>]+>/g,"");

for(j=0;j<filter.length;j++){
	if ((filter[j].length < 5 && s1 === (filter[j])) || (filter[j].length > 4 && (s2.indexOf(filter[j]) > -1 || s3.indexOf(filter[j]) > -1 || s4.indexOf(filter[j]) > -1)) || input.value.toUpperCase() == "") {
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