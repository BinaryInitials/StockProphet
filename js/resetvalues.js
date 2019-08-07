function filterFunction() {
var min1, max1, min2, max2, min3, max3, min4, max4, min5, max5, min6, max6, min7, max7, min8, max8, table;
var tr, td1, td2, td3, td4, td5, td6, td7, td8, i;
var input, filter, table, te1, te2, te3, te4;
input = document.getElementById("filter-search");
filter = input.value.toUpperCase().split(" ");
min1 = document.getElementById("mktcap-min").value;
max1 = document.getElementById("mktcap-max").value;
min2 = document.getElementById("linear-min").value;
max2 = document.getElementById("linear-max").value;
min3 = document.getElementById("rigid-min").value;
max3 = document.getElementById("rigid-max").value;
min4 = document.getElementById("growth-min").value;
max4 = document.getElementById("growth-max").value;
min5 = document.getElementById("mva-min").value;
max5 = document.getElementById("mva-max").value;
min6 = document.getElementById("bband-min").value;
max6 = document.getElementById("bband-max").value;
min7 = document.getElementById("oidr-min").value;
max7 = document.getElementById("oidr-max").value;
min8 = document.getElementById("midr-min").value;
max8 = document.getElementById("midr-max").value;

table = document.getElementById("myTable");
tr = table.getElementsByTagName("tr");
for (i = 0; i < tr.length; i++) {
te1= tr[i].getElementsByTagName("td")[2];
te2= tr[i].getElementsByTagName("td")[3];
te3= tr[i].getElementsByTagName("td")[4];
te4= tr[i].getElementsByTagName("td")[5];
td1 = tr[i].getElementsByTagName("td")[9];
td2 = tr[i].getElementsByTagName("td")[10];
td3 = tr[i].getElementsByTagName("td")[11];
td4 = tr[i].getElementsByTagName("td")[12];
td5 = tr[i].getElementsByTagName("td")[13];
td6 = tr[i].getElementsByTagName("td")[14];
td7 = tr[i].getElementsByTagName("td")[15];
td8 = tr[i].getElementsByTagName("td")[16];

var exist = false;
if (te1 && te2 && te3 && te4) {
var s1 = te1.innerHTML.toUpperCase().replace(/<[^>]+>/g,"").replace(/&[a-z]+; /g,"").toUpperCase().trim();
var s2 = te2.innerHTML.toUpperCase().replace(/<[^>]+>/g,"").replace(/&[a-z]+; /g,"").toUpperCase().trim();
var s3 = te3.innerHTML.toUpperCase().replace(/<[^>]+>/g,"").replace(/&[a-z]+; /g,"").toUpperCase().trim();
var s4 = te4.innerHTML.toUpperCase().replace(/<[^>]+>/g,"").replace(/&[a-z]+; /g,"").toUpperCase().trim();
for(j=0;j<filter.length;j++){
if ((filter[j].length < 5 && s1 === (filter[j])) || (filter[j].length > 4 && (s2.indexOf(filter[j]) > -1 || s3.indexOf(filter[j]) > -1 || s4.indexOf(filter[j]) > -1)) || input.value== "") {
exist = true;
}
}
}
if (td1 && td2 && td3 && td4 && td5 && td6 && td7 && td8){
if (
Number(td1.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min1) &&
Number(td1.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max1) &&
Number(td2.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min2) &&
Number(td2.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max2) &&
Number(td3.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min3) &&
Number(td3.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max3) &&
Number(td4.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min4) &&
Number(td4.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max4) &&
Number(td5.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min5) &&
Number(td5.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max5) &&
Number(td6.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min6) &&
Number(td6.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max6) &&
Number(td7.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min7) &&
Number(td7.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max7) &&
Number(td8.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min8) &&
Number(td8.innerHTML.replace(/%$/g,"").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max8) &&
exist
){
tr[i].style.display = "";
} else {
tr[i].style.display = "none";
}
}
}
}
