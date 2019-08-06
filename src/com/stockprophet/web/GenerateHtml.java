package com.stockprophet.web;

import static com.stockprophet.web.Methods.convertToColor;
import static com.stockprophet.web.Methods.darken;
import static com.stockprophet.web.Methods.generateColorMap;
import static com.stockprophet.web.Methods.generateColorsFromColorMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GenerateHtml {
	
	
	public static List<HashMap<Employee, String>> loadEmployees(String teamDescription) throws IOException{
		BufferedReader buffer = new BufferedReader(new FileReader(new File("txt/bios.txt")));
		teamDescription = buffer.readLine();
		String line;
		List<HashMap<Employee, String>> employees = new ArrayList<HashMap<Employee, String>>();
		List<String> bios = new ArrayList<String>();
		while((line=buffer.readLine())!=null)
			bios.add(line);
		buffer.close();
		for(int i=0;i<bios.size();i++)
			if(i%4==3){
				HashMap<Employee, String> employee = new HashMap<Employee, String>();
				employee.put(Employee.NAME, bios.get(i-3));
				employee.put(Employee.TITLE, bios.get(i-2));
				employee.put(Employee.IMAGE, bios.get(i-1));
				employee.put(Employee.DESCRIPTION, bios.get(i-0));
				employees.add(employee);
			}
		return employees;
	}
	
	
	public static void writeMeetTheTeam() throws IOException{
		String teamDescription = "";
		List<HashMap<Employee, String>> employees = loadEmployees(teamDescription); 
		File file = new File("MeetTheTeam.php");
		List<String> phpLogCode = loadPhpLog("meettheteam_log.html");
		file.createNewFile();
		FileWriter writer = new FileWriter(file.getAbsoluteFile());
		BufferedWriter buffer = new BufferedWriter(writer);
		for(String phpLog : phpLogCode)
			buffer.write(phpLog);
		buffer.write("<!DOCTYPE html>\n");
		buffer.write("<html><head><meta name=\"viewport\" http-equiv=\"Content-Type\" content=\"width=device-width, initial-scale=1\">\n");
		buffer.write("<title>Stock Prophet</title>\n");
		buffer.write("<link rel=\"stylesheet\" href=\"css/c13.css\">\n");
		
		buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\" />");
		buffer.write("</head>\n");
		buffer.write("<body link=\"blue\">\n");
		buffer.write("<div class=\"container\">\n");
		buffer.write("<form>\n");
		buffer.write("<div class=\"one-third column\">\n");
		buffer.write("<img src=\"images/logo.png\" alt=\"logo\" id=\"logo\">");
		buffer.write("<h2 class=\"welcome\">Meet The Team</h2>\n");
		buffer.write("<p class=\"intro\" align=\"justify\">" + teamDescription + "</p>");
		buffer.write("<center></center></div>\n");
		
		for(HashMap<Employee, String> employee : employees){
			buffer.write("<div class=\"one-third column\">\n");
			buffer.write("<h2>" + employee.get(Employee.NAME) + "</h2>\n");
			buffer.write("<div class=\"name\">\n");
			buffer.write("<label for=\"name\" class=\"required\">" + employee.get(Employee.TITLE) + "</label>\n");
			buffer.write("<center><img src=\"images/" + employee.get(Employee.IMAGE) + "\" style=\"width:250px;height:350px;\"></center>\n");
			buffer.write("</div>\n");
			buffer.write("<p class=\"intro\" align=\"justify\">" + employee.get(Employee.DESCRIPTION) + "</p>\n");
			buffer.write("</div>\n");
		}
		buffer.write("</form></div></body></html>\n");
		buffer.close();
	}
	
	public static void writePlotHtml() throws IOException{
		File file = new File("financeChart.php");
		List<String> phpLogCode = loadPhpLog("plot-log.html");
		file.createNewFile();
		FileWriter writer = new FileWriter(file.getAbsoluteFile());
		BufferedWriter buffer = new BufferedWriter(writer);
		
		for(String phpLog : phpLogCode)
			buffer.write(phpLog);
		buffer.write("<head>\n");
		
		buffer.write("<meta charset=\"utf-8\">\n");
		buffer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n");

		buffer.write("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n");
		buffer.write("<script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n");
		buffer.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\" integrity=\"sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1\" crossorigin=\"anonymous\"></script>\n");
		buffer.write("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\" integrity=\"sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM\" crossorigin=\"anonymous\"></script>\n");
		
		buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\" />\n");
		buffer.write("<script src=\"https://cdn.plot.ly/plotly-latest.min.js\"></script>\n");
		buffer.write("<script src=\"js/candlestick.js\"></script>\n");
		buffer.write("</head>\n");
		buffer.write("<body>\n");
		
		buffer.write("<?php\n");
		buffer.write("$symbol = $_GET['s'];\n");
		buffer.write("chdir('/home/pi/StockProphet/');\n");
		buffer.write("$output = shell_exec('sudo ./get-data.sh ' . $symbol);\n");
		buffer.write("$output = shell_exec('sudo mv ' . $symbol . '.json /var/www/html/stockprophet/json/');\n");
		buffer.write("?>\n");
		
		buffer.write("<div id=\"graph\">\n</div>\n");
		buffer.write("<script>\n");
		buffer.write("plot(location.search.substring(1).replace(/.*=/g,''));\n");
		buffer.write("</script>\n");
		buffer.write("</body>\n");
		
		buffer.close();
	}
	
	public static List<String> loadPhpLog(String fileName){
		List<String> phpLogCode = new ArrayList<String>();
		phpLogCode.add("<?php\n");
		phpLogCode.add("$file = fopen(\"" + fileName + "\", \"a\") or die(\"Unable to open file!\");\n");
		phpLogCode.add("fwrite($file, date('Y-m-d|H:i:s'));\n");
		phpLogCode.add("fwrite($file,\"|\");\n");
		phpLogCode.add("fwrite($file, $_SERVER[\"HTTP_REFERER\"]);\n");
		phpLogCode.add("fwrite($file,\"|\");\n");
		phpLogCode.add("fwrite($file, $_SERVER[\"HTTP_HOST\"]);\n");
		phpLogCode.add("fwrite($file,\"|\");\n");
		phpLogCode.add("fwrite($file, $_SERVER[\"REMOTE_ADDR\"]);\n");
		phpLogCode.add("fwrite($file,\"|\");\n");
		phpLogCode.add("fwrite($file, $_SERVER['HTTP_USER_AGENT']);\n");
		phpLogCode.add("fwrite($file,'<br>\n');\n");
		phpLogCode.add("fclose( $file );\n");
		phpLogCode.add("?>\n");
		return phpLogCode;
	}
	
	public static void writeJSfiles() {
		
		File file1 = new File("js/resetvalues.js");
		File file2 = new File("js/filterFunction.js");
		try{
			{
				file1.createNewFile();
				BufferedWriter buffer1 = new BufferedWriter(new FileWriter(file1.getAbsoluteFile()));
				buffer1.write("function resetValues() {\n");
				for(Column column : Column.values())
					if(column.isFilterable())
						buffer1.write(
							"document.getElementById(\"" + column.name().toLowerCase() + "-min\").value = " + column.getMin() + ";\n" +
							"document.getElementById(\"" + column.name().toLowerCase() + "-max\").value = " + column.getMax() + ";\n"
						);
				buffer1.write("filterFunction();\n");
				buffer1.write("}\n");
				buffer1.close();
			}
		
			file2.createNewFile();
			BufferedWriter buffer2 = new BufferedWriter(new FileWriter(file1.getAbsoluteFile()));
			buffer2.write("function filterFunction() {\n");
			String varRow = "var ";
			int i=1;
			for(Column column : Column.values())
				if(column.isFilterable())
					varRow += "min" + i + ", max" + i++ + ", ";
			varRow += "table;\n";
			buffer2.write(varRow);
		
			String varRow2 = "var tr, ";
			i=1;
			for(Column column : Column.values())
				if(column.isFilterable())
					varRow2 += "td" + i++ + ", ";
			varRow2 += "i;\n";
			buffer2.write(varRow2);
			buffer2.write("var input, filter, table, te1, te2, te3, te4;\n");
			buffer2.write("input = document.getElementById(\"filter-search\");\n");
			buffer2.write("filter = input.value.toUpperCase().split(\" \");\n");
			i=1;
			for(Column column : Column.values())
				if(column.isFilterable()){
					buffer2.write("min" + i + " = document.getElementById(\"" + column.name().toLowerCase() +"-min\").value;\n");
					buffer2.write("max" + i + " = document.getElementById(\"" + column.name().toLowerCase() +"-max\").value;\n");
					i++;
				}
			buffer2.write("\n");
			buffer2.write("table = document.getElementById(\"myTable\");\n");
			buffer2.write("tr = table.getElementsByTagName(\"tr\");\n");
			buffer2.write("for (i = 0; i < tr.length; i++) {\n");
			for(int j=0;j<4;j++)
				buffer2.write("te" + (j+1) + "= tr[i].getElementsByTagName(\"td\")[" + (j+2) + "];\n");
		
			int columnIndex=0;
			i=1;
			for(Column column : Column.values()){
				if(column.isFilterable())
					buffer2.write("td" + i++ + " = tr[i].getElementsByTagName(\"td\")[" + columnIndex + "];\n");
				columnIndex++;
			}
		
			buffer2.write("\n");
		
			//First if of text
			buffer2.write("var exist = false;\n");
			buffer2.write("if (te1 && te2 && te3 && te4) {\n");
			for(int j=0;j<4;j++)
				buffer2.write("var s" + (j+1) +" = te" + (j+1) + ".innerHTML.toUpperCase().replace(/<[^>]+>/g,\"\").replace(/&[a-z]+; /g,\"\").toUpperCase().trim();\n");
			buffer2.write("for(j=0;j<filter.length;j++){\n");
			buffer2.write("if ((filter[j].length < 5 && s1 === (filter[j])) || (filter[j].length > 4 && (s2.indexOf(filter[j]) > -1 || s3.indexOf(filter[j]) > -1 || s4.indexOf(filter[j]) > -1)) || input.value== \"\") {\n");
			buffer2.write("exist = true;\n");
			buffer2.write("}\n");
			buffer2.write("}\n");
			buffer2.write("}\n");
		
		
		String conditionRow = "if (";
		i=1;
		for(Column column : Column.values())
			if(column.isFilterable())
				conditionRow += "td" + i++ + " && "; 
		conditionRow = conditionRow.replaceAll(" && $", "){\n");
		
		buffer2.write(conditionRow);
		buffer2.write("if (\n");
		
		String giantCondition = "";
		i=1;
		for(Column column : Column.values()){
			if(column.isFilterable()){
				giantCondition += "Number(td" + i + ".innerHTML.replace(/%$/g,\"\").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min" + i +") &&\n";
				giantCondition += "Number(td" + i + ".innerHTML.replace(/%$/g,\"\").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max" + i +") &&\n";
				i++;
			}
		}
		giantCondition += "exist\n){\n";
		buffer2.write(giantCondition);
		buffer2.write("tr[i].style.display = \"\";\n");
		buffer2.write("} else {\n");
		buffer2.write("tr[i].style.display = \"none\";\n");
		buffer2.write("}\n");
		buffer2.write("}\n");
		buffer2.write("}\n");
		buffer2.write("}\n");
		buffer2.close();
		}catch(IOException e) {
			
		}
	}
	
	
	public static void writeWebBootstrap(List<HashMap<Column, String>> data) {
		File file = new File("index.php");
		Date timestamp = new Date();
		SimpleDateFormat sdt = new SimpleDateFormat("MM.dd.YY");
		List<String> phpLogCode = loadPhpLog("stockprophet_log.html");
		try{
			file.createNewFile();
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(writer);
			for(String phpLog : phpLogCode)
				buffer.write(phpLog);
						
			buffer.write("<!DOCTYPE html>\n");
			buffer.write("<html lang=\"en\">\n");
			buffer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">");
			buffer.write("<head>\n");
			buffer.write("<title> STOCK PROPHET </title>\n");
			
			//Loading bootstrap CSS files
			buffer.write("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n");

			//Loading icon
			buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\">\n");

			//Loading bootstrap JS files
			buffer.write("<script src=\"https://code.jquery.com/jquery<span class=\"badge badge-danger\">-3</span>.3.1.slim.min.js\" integrity=\"sha384-q8i/X<span class=\"badge badge-success\">+0</span>65DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH<span class=\"badge badge-success\">+0</span>abtTE1Pi6jizo\" crossorigin=\"anonymous\" defer></script>\n");
			buffer.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\" integrity=\"sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1\" crossorigin=\"anonymous\" defer></script>\n");
			buffer.write("<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\" integrity=\"sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM\" crossorigin=\"anonymous\" defer></script>");
			buffer.write("<script src=\"js/candlestick.js\" defer></script>\n");
			buffer.write("<script src=\"js/search.js\" defer></script>\n");
			buffer.write("<script src=\"js/searchIndustry.js\" defer></script>\n");
			buffer.write("<script src=\"js/searchSector.js\" defer></script>\n");
			buffer.write("<script src=\"js/jquery.js\" defer></script>\n");
			buffer.write("<script src=\"js/sort-table.js\" defer></script>\n");
			buffer.write("<script src=\"js/resetvalues.js\" defer></script>\n");
			buffer.write("<script src=\"js/filterFunction.js\" defer></script>\n");
			buffer.write("<style>\n");
			buffer.write("body {\n");
			buffer.write("background-image: linear-gradient(91deg, #FFFFFF, #BBBBBB);\n");
			buffer.write("}\n");
			buffer.write("table {\n");
			buffer.write("border-collapse: separate;\n");
			buffer.write("border-spacing: 0.125em;\n");
			buffer.write("}");
			buffer.write("</style>\n");
			
			buffer.write("</head>\n");
			buffer.write("<body>\n");
			buffer.write("<div class=\"container\" id=\"myHeader\">\n");
			buffer.write("<div class=\"container\">\n");
			buffer.write("<h2>Stock Prophet</h2><h3>" + sdt.format(timestamp) + "</h3>\n");
			buffer.write("</div>\n");
			
			
			
			buffer.write("<div class=\"container\">\n");
			buffer.write("<table id=\"setting-table\" class=\"table table-bordered border border-dark\">\n");
			buffer.write("<thead>\n");
			buffer.write("<tr>\n");
			for(Column column : Column.values())
				if(column.isFilterable())
					buffer.write("<th scope=\"col\">" + column.name() + "</th>\n");
			buffer.write("</tr>\n");
			buffer.write("</thead>\n");
			
			buffer.write("<tbody>\n");
			buffer.write("<tr scope=\"row\">\n");
			for(Column column : Column.values())
				if(column.isFilterable())
					buffer.write(
							"<td id=\"metric-range\">" +
							"<input class=\"form-control\" type=\"text\" id=\"" + column.name().toLowerCase() + "-min\" onkeyup=\"filterFunction()\" value=\"" + column.getMin() + "\"/>" +
							"<input class=\"form-control\" type=\"text\" id=\"" + column.name().toLowerCase() + "-max\" onkeyup=\"filterFunction()\" value=\"" + column.getMax() + "\"/>" + 
							"</td>\n");
			buffer.write("</tr>\n");
			buffer.write("</tbody>\n");
			buffer.write("</table>\n");
			buffer.write("</div>\n");
			
			/*
			 * Search bar
			 */
			buffer.write("<div id=\"search\" class=\"container\">\n");
			buffer.write("<div class=\"input-group mb-3\">\n");
			buffer.write("<div class=\"input-group-prepend\">\n");
			buffer.write("<span class=\"input-group-text\" id=\"inputGroup-sizing-default\">Search</span>\n");
			buffer.write("</div>\n\n");
			buffer.write("<input type=\"text\" class=\"form-control\" onkeyup=\"search()\" id=\"filter-search\" aria-label=\"Default\" aria-describedby=\"inputGroup-sizing-default\">\n");
			buffer.write("<button class=\"btn btn-danger\" onclick=\"resetValues()\">RESET</button>\n");
			buffer.write("</div>\n");
			buffer.write("</div>\n");
			buffer.write("</div>\n");
			
			/*
			 * The table with data
			 */
			
			buffer.write("<div class=\"container\">\n");
			buffer.write("<div class=\"content\">\n");
			buffer.write("<table id=\"myTable\" class=\"sortable table table-hover table-striped table-wrapper-scroll-y table-bordered border border-dark table-responsive\">\n");
			buffer.write("<thead>\n<tr>\n");
			
			for(Column column : Column.values())
				buffer.write("<th data-sort=\"" + (column.isNumber()? "number" : "name") + "\">" + column.name().toUpperCase() + "</th>");

			buffer.write("</tr>\n</thead>\n<tbody>\n");
			
			for(int row=0;row<data.size();row++){
				int diff = Integer.valueOf(data.get(row).get(Column.DIFF));
				String sign = "+";
				String badgeType = "secondary";
				if(diff > 0) {
					badgeType = "success";
				}else if(diff < 0) {
					badgeType = "danger";
				}
				String tableRow = "<tr>";
				String symbol = data.get(row).get(Column.SYMB);
				for(Column column : Column.values()){
					if(column == Column.RANK){
						tableRow += "<td>" + (row+1) + "</td>";
					}else if(column == Column.DIFF){
						tableRow += "<td><span class=\"badge badge-" + badgeType + "\">" + sign + diff + "</span></td>";
					}else if(column == Column.SYMB){
						tableRow += "<td><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" + symbol + "</td>";
					}else if(column == Column.COMPANY){
						tableRow += "<td><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" +  data.get(row).get(Column.COMPANY) + "</td>";
					}else if(column == Column.SECTOR){
						tableRow += "<td onclick=\"searchSector('" + data.get(row).get(Column.SECTOR) + "')\">" +  data.get(row).get(Column.SECTOR) + "</td>";
					}else if(column == Column.INDUSTRY){
						tableRow += "<td onclick=\"searchIndustry('" + data.get(row).get(Column.INDUSTRY) + "')\">" +  data.get(row).get(Column.INDUSTRY) + "</td>";
					}else{
						if(data.get(row).get(column) != null){
							if( column == Column.PRICE ){
								tableRow += "<td><a href=\"financeChart.php?s=" + symbol + "\" target=\"_blank\">" + String.format("%.2f", Double.valueOf(data.get(row).get(column))) + "</td>";
							}else{
								tableRow += "<td>" + String.format("%.2f", Double.valueOf(data.get(row).get(column))) + "</td>";
							}
						}else{
							tableRow += "<td>null</td>";
						}
					}
				}
				tableRow += "</tr>\n";
				buffer.write(tableRow);
			}
			
			//The end.
			buffer.write("</tbody></table></div></div></body></html>");
			buffer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void writeWeb(List<HashMap<Column, String>> data) {
		File file = new File("index.php");
		Date timestamp = new Date();
		SimpleDateFormat sdt = new SimpleDateFormat("MM.dd.YY");
		List<String> phpLogCode = loadPhpLog("stockprophet_log.html");
		try{
			file.createNewFile();
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(writer);
			for(String phpLog : phpLogCode)
				buffer.write(phpLog);
						
			buffer.write("<!DOCTYPE html>\n");
			buffer.write("<html>\n");
			buffer.write("<head>\n");
			buffer.write("<title> STOCK PROPHET </title>\n");
			List<String> cssFiles = getWebFiles(WebFileType.CSS);
			for(String cssFile : cssFiles)
				buffer.write("<link rel=\"stylesheet\" href=\"css/"+ cssFile + "\" />\n");

			buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\" />");
			
			buffer.write("</head>\n");
			buffer.write("<body>\n");
			buffer.write("<div class=\"header\" id=\"myHeader\">\n");
			buffer.write("<ul>\n");
			
			/*
			 * This line allows the settings table to scroll horizontally. 
			 * It's a wonder why the main table doesn't need it and yet can
			 * still scroll that way.
			 */ 
			buffer.write("<div style=\"overflow-x:auto;\">\n");
			
			buffer.write("<table id=\"setting-table\">\n");
			buffer.write("<thead>\n");
			buffer.write("<tr>\n");
			
			
			for(Column column : Column.values())
				if(column.isFilterable())
					buffer.write("<th>" + column.name() + "</th>\n");
			
			buffer.write("</tr>\n");
			buffer.write("</thead>\n");
			buffer.write("<h2>Stock Prophet: " + sdt.format(timestamp) + "</h2>");
			buffer.write("<tbody>\n");
			buffer.write("<tr>\n");
			
			for(Column column : Column.values())
				if(column.isFilterable())
					buffer.write(
							"<td><div id=\"metric-range\" style=\"text-align:left\">" +
							"MIN: <input type=\"text\" id=\"" + column.name().toLowerCase() + "-min\" onkeyup=\"filterFunction()\" value=\"" + column.getMin() + "\"/>" +
							"MAX: <input type=\"text\" id=\"" + column.name().toLowerCase() + "-max\" onkeyup=\"filterFunction()\" value=\"" + column.getMax() + "\"/>" + 
							"</div></td>\n");

			buffer.write("</tr>\n");
			buffer.write("</tbody>\n");
			buffer.write("</table>\n");

			//Closing the div block that allows the table to move horizontally
			buffer.write("</div>\n");
			
			buffer.write("<div align=\"center\">\n");
			buffer.write("<input id=\"checkboxId\" type=\"checkbox\" onclick=\"resetValues()\" name=\"resetFilterValues\"/><div class=\"tooltip\">Reset Values<span class=\"tooltiptext\">Click twice to reset values</span></div>\n");
			buffer.write("</div>");
			
			buffer.write("<br>\n");
			buffer.write("<br>\n");
			buffer.write("<div id=\"search\">\n");
			buffer.write("<input type=\"text\" size=\"2\" placeholder=\"Search...\" onkeyup=\"search()\" id=\"filter-search\" style=\"width: 500px;\"/>\n");
			buffer.write("</div>\n");
			buffer.write("</div>\n");
			
			buffer.write("<div class=\"content\">\n");
			buffer.write("<table id=\"myTable\" class=\"sortable\">");
			
			String tableHeader = "<thead><tr>";
			for(Column column : Column.values())
				tableHeader += "<th data-sort=\"" + (column.isNumber()? "number" : "name") + "\">" + column.name().toUpperCase() + "</th>";

			tableHeader += "</tr></thead>\n";
			buffer.write(tableHeader);
			buffer.write("<tbody>\n");
			
			HashMap<Integer, int[]> colorMap = generateColorMap();

			for(int row=0;row<data.size();row++){
				String rankQualifier = "equal";
				int diff = Integer.valueOf(data.get(row).get(Column.DIFF));
				String sign = "+";
				
				if(diff > 0){
					rankQualifier = "rise";
				}else if(diff < 0){
					rankQualifier = "fall";
					sign = "";
				}
				double score = (data.size() - Integer.valueOf(data.get(row).get(Column.RANK))-1)/(double)data.size();
				int[] color = generateColorsFromColorMap(score, colorMap);
				
				String tableRow = "<tr>";
				int columnIndex=0;
				String symbol = data.get(row).get(Column.SYMB);
				for(Column column : Column.values()){
					if(column == Column.RANK){
						tableRow += "<td  class=\"tbl-prevrank-icon\" style=\"background-color: #" + convertToColor(color) +  "\">" + (row+1) + "    <span class=\"rank-" + rankQualifier + "\" />" + "</td>";
					}else if(column == Column.DIFF){
						tableRow += "<td style=\"background-color: #" + convertToColor(color) + "\">" + sign + diff + "</td>";
					}else if(column == Column.SYMB){
						tableRow += "<td style=\"background-color: #" + convertToColor(color) + "\"><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" + symbol + "</td>";
					}else if(column == Column.COMPANY){
						tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\"><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" +  data.get(row).get(Column.COMPANY) + "</td>";
					}else if(column == Column.SECTOR){
						tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\" onclick=\"searchSector('" + data.get(row).get(Column.SECTOR) + "')\">" +  data.get(row).get(Column.SECTOR) + "</td>";
					}else if(column == Column.INDUSTRY){
						tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\" onclick=\"searchIndustry('" + data.get(row).get(Column.INDUSTRY) + "')\">" +  data.get(row).get(Column.INDUSTRY) + "</td>";
					}else{
						if(data.get(row).get(column) != null){
							if( column == Column.PRICE ){
								tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\"><a href=\"financeChart.php?s=" + symbol + "\" target=\"_blank\">" + String.format("%.2f", Double.valueOf(data.get(row).get(column))) + "</td>";
							}else{
								tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\">" + String.format("%.2f", Double.valueOf(data.get(row).get(column))) + "</td>";
							}
						}else{
							tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\">null</td>";
						}
					}
					columnIndex++;
				}
				tableRow += "</tr>\n";
				buffer.write(tableRow);
			}
			buffer.write("</tbody>\n");
			buffer.write("</table>\n");
			
			//Javascript
			buffer.write("<script>\n");
			
			buffer.write("function resetValues() {\n");
			buffer.write("document.getElementById(\"checkboxId\").onclick = function() {\n");
			buffer.write("if (this.checked) {\n");
			for(Column column : Column.values())
				if(column.isFilterable())
					buffer.write(
							"document.getElementById(\"" + column.name().toLowerCase() + "-min\").value = " + column.getMin() + ";\n" +
							"document.getElementById(\"" + column.name().toLowerCase() + "-max\").value = " + column.getMax() + ";\n"
						);

			buffer.write("filterFunction();\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			
			buffer.write("function filterFunction() {\n");
			String varRow = "var ";
			int i=1;
			for(Column column : Column.values())
				if(column.isFilterable())
					varRow += "min" + i + ", max" + i++ + ", ";
			varRow += "table;\n";
			buffer.write(varRow);
			
			String varRow2 = "var tr, ";
			i=1;
			for(Column column : Column.values())
				if(column.isFilterable())
					varRow2 += "td" + i++ + ", ";
			varRow2 += "i;\n";
			buffer.write(varRow2);
			buffer.write("var input, filter, table, te1, te2, te3, te4;\n");
			buffer.write("input = document.getElementById(\"filter-search\");\n");
			buffer.write("filter = input.value.toUpperCase().split(\" \");\n");
			i=1;
			for(Column column : Column.values())
				if(column.isFilterable()){
					buffer.write("min" + i + " = document.getElementById(\"" + column.name().toLowerCase() +"-min\").value;\n");
					buffer.write("max" + i + " = document.getElementById(\"" + column.name().toLowerCase() +"-max\").value;\n");
					i++;
				}
			buffer.write("\n");
			buffer.write("table = document.getElementById(\"myTable\");\n");
			buffer.write("tr = table.getElementsByTagName(\"tr\");\n");
			buffer.write("for (i = 0; i < tr.length; i++) {\n");
			for(int j=0;j<4;j++)
				buffer.write("te" + (j+1) + "= tr[i].getElementsByTagName(\"td\")[" + (j+2) + "];\n");
			
			int columnIndex=0;
			i=1;
			for(Column column : Column.values()){
				if(column.isFilterable())
					buffer.write("td" + i++ + " = tr[i].getElementsByTagName(\"td\")[" + columnIndex + "];\n");
				columnIndex++;
			}
			
			
			//td.innerHTML.replace(/<[^>]+>/g,"").replace(/&[a-z]+; /g,"").toUpperCase().trim()
			
			buffer.write("\n");
			
			//First if of text
			buffer.write("var exist = false;\n");
			buffer.write("if (te1 && te2 && te3 && te4) {\n");
			for(int j=0;j<4;j++)
				buffer.write("var s" + (j+1) +" = te" + (j+1) + ".innerHTML.toUpperCase().replace(/<[^>]+>/g,\"\").replace(/&[a-z]+; /g,\"\").toUpperCase().trim();\n");
			buffer.write("for(j=0;j<filter.length;j++){\n");
			buffer.write("if ((filter[j].length < 5 && s1 === (filter[j])) || (filter[j].length > 4 && (s2.indexOf(filter[j]) > -1 || s3.indexOf(filter[j]) > -1 || s4.indexOf(filter[j]) > -1)) || input.value== \"\") {\n");
			buffer.write("exist = true;\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			
			
			String conditionRow = "if (";
			i=1;
			for(Column column : Column.values())
				if(column.isFilterable())
					conditionRow += "td" + i++ + " && "; 
			conditionRow = conditionRow.replaceAll(" && $", "){\n");
			
			buffer.write(conditionRow);
			buffer.write("if (\n");
			
			String giantCondition = "";
			i=1;
			for(Column column : Column.values()){
				if(column.isFilterable()){
					giantCondition += "Number(td" + i + ".innerHTML.replace(/%$/g,\"\").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) > Number(min" + i +") &&\n";
					giantCondition += "Number(td" + i + ".innerHTML.replace(/%$/g,\"\").replace(/N.A/,'-1').replace(/<[^>]+>/g,'')) < Number(max" + i +") &&\n";
					i++;
				}
			}
			giantCondition += "exist\n){\n";
			buffer.write(giantCondition);
			buffer.write("tr[i].style.display = \"\";\n");
			buffer.write("} else {\n");
			buffer.write("tr[i].style.display = \"none\";\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("</script>\n");
			
			buffer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\n");
			
			List<String> jsFiles = getWebFiles(WebFileType.JS);
			Collections.sort(jsFiles);
			for(String jsFile : jsFiles)
				buffer.write("<script src=\"js/" + jsFile + "\"></script>\n");

			
			
			buffer.write("</ul>\n");
			buffer.write("<script type=\"text/javascript\" src=\"js/prettify.js\"></script>\n");
			buffer.write("</body>\n");
			buffer.write("</html>\n");
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	enum WebFileType {
		CSS,
		JS
	}
	
	private static List<String> getWebFiles(WebFileType wbf){
		String extension = wbf.toString().toLowerCase();
		File folder = 	new File(extension + "/");
		List<String> files = new ArrayList<String>();
		for(File file : folder.listFiles())
			if(file.isFile() && file.getName().endsWith(extension))
				files.add(file.getName().replaceAll(".*/", ""));
		Collections.sort(files);
		return files;
	}
}