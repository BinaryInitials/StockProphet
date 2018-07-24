package com.stockprophet.web;

import static com.stockprophet.web.Methods.convertToColor;
import static com.stockprophet.web.Methods.darken;
import static com.stockprophet.web.Methods.generateColorMap;
import static com.stockprophet.web.Methods.generateColorsFromColorMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class GenerateHtml {
	
	
	public static void writeMobile() throws IOException{
		File file = new File("mobile.php");
		List<String> phpLogCode = loadPhpLog(false);
		file.createNewFile();
		FileWriter writer = new FileWriter(file.getAbsoluteFile());
		BufferedWriter buffer = new BufferedWriter(writer);
		
		for(String phpLog : phpLogCode)
			buffer.write(phpLog);
		
		buffer.write("<!DOCTYPE html>\n");
		buffer.write("<html>\n");
		buffer.write("<head>\n");
		buffer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /> ");
		buffer.write("<title> STOCK PROPHET </title>\n");
		List<String> cssFiles = getWebFiles(WebFileType.CSS, true);
		for(String cssFile : cssFiles)
			buffer.write("<link rel=\"stylesheet\" href=\"css/"+ cssFile + "\" />\n");

		buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\" />\n");
		buffer.write("</head>\n");
		buffer.write("<body>\n");
		buffer.write("<div class=\"header\" id=\"myHeader\">\n");
		
		//This is where the navbar will be in place
		buffer.write("<div class=\"navbar\" id=\"myTopnav\">\n");
		buffer.write("<div class=\"dropdown\">\n");
		buffer.write("<div class=\"dropbtn\" onclick=\"dropDown()\">Release<i class=\"fa fa-caret-down\"></i></div>\n");
		buffer.write("<div class=\"dropdown-content\" id=\"myDropDown\">\n");
		
		Path path = Paths.get("data/AAPL.csv");
		BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
		String dataTimestamp = attr.creationTime().toString().replaceAll("([0-9]{4}-[0-9]{2}-[0-9]{2})T([0-9]{2}:[0-9]{2}:[0-9]{2}).*", "$1 $2");
		try {
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			buffer.write("<a href=\"#\">Data: " + sdf.parse(dataTimestamp).toString() + "</a>\n");
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		
		Date date = new Date();
		buffer.write("<a href=\"#\">Website: " + date.toString() + "</a>\n");
		buffer.write("</div>\n");
		buffer.write("</div>\n");
		
		buffer.write("<div class=\"dropdown\">\n");
		buffer.write("<div class=\"dropbtn\" onclick=\"dropDown()\">About<i class=\"fa fa-caret-down\"></i></div>\n");
		buffer.write("<div class=\"dropdown-content\" id=\"myDropDown\">\n");
		buffer.write("<a href=\"MeetTheTeam.php\">Meet The Team</a>\n");
		buffer.write("</div>\n");
		buffer.write("</div>\n");
		
		
		buffer.write("<div class=\"dropdown\">\n");
		buffer.write("<div class=\"dropbtn\" onclick=\"dropDown()\">Contact<i class=\"fa fa-caret-down\"></i></div>\n");
		buffer.write("<div class=\"dropdown-content\" id=\"myDropDown\">\n");
		buffer.write("<a href=\"mailto:stockprophetcontact@gmail.com\">stockprophetcontact@gmail.com</a>\n");
		buffer.write("</div>\n");
		buffer.write("</div>\n");
		buffer.write("<div class=\"dropdown\">\n");
		buffer.write("<div class=\"dropbtn\" onclick=\"dropDown()\">Metrics<i class=\"fa fa-caret-down\"></i></div>\n");
		buffer.write("<div class=\"dropdown-content\" id=\"myDropDown\">\n");
		
		buffer.write("<a href=\"#\" onclick=\"loadFromJSON('" + Column.DAY5.toString() + "')\">" + Column.DAY5.toString() + "</a>\n");
		buffer.write("<a href=\"#\" onclick=\"loadFromJSON('" + Column.MONTH1.toString() + "')\">" + Column.MONTH1.toString() + "</a>\n");
		buffer.write("<a href=\"#\" onclick=\"loadFromJSON('" + Column.MONTH6.toString() + "')\">" + Column.MONTH6.toString() + "</a>\n");
		buffer.write("<a href=\"#\" onclick=\"loadFromJSON('" + Column.YEAR1.toString() + "')\">" + Column.YEAR1.toString() + "</a>\n");
		buffer.write("<a href=\"#\" onclick=\"loadFromJSON('" + Column.YEAR5.toString() + "')\">" + Column.YEAR5.toString() + "</a>\n");
		
		buffer.write("</div>\n");
		buffer.write("</div>\n");
		buffer.write("</div>\n");
		
		buffer.write("<ul>\n");
		
		buffer.write("<div id=\"search\">\n");
		buffer.write("<input type=\"text\" size=\"2\" placeholder=\"Search...\" onkeyup=\"search()\" id=\"filter-search\" />\n");
		buffer.write("</div>\n");
		buffer.write("</ul>\n");
		buffer.write("</div>\n");
		
		buffer.write("<div class=\"content\" id=\"content\">\n");
		buffer.write("</div>\n");
		//Javascript
		
		List<String> jsFiles = getWebFiles(WebFileType.JS, true);
		Collections.sort(jsFiles);
		for(String jsFile : jsFiles)
			buffer.write("<script src=\"js/" + jsFile + "\"></script>\n");

		buffer.write("<script type=\"text/javascript\" src=\"js/prettify.js\"></script>\n");
		buffer.write("</body>\n");
		buffer.write("</html>\n");
		buffer.close();
	}
	
	public static List<String> loadPhpLog(boolean isMobile){
		List<String> phpLogCode = new ArrayList<String>();
		String fileName = isMobile ? "stockprophet_mobile_log.html" : "stockprophet_log.html";
		phpLogCode.add("<?php\n");
		phpLogCode.add("$file = fopen(\"" + fileName + "\", \"a\") or die(\"Unable to open file!\");\n");
		phpLogCode.add("fwrite($file, date('Y-m-d|H:i:s'));\n");
		phpLogCode.add("fwrite($file,\"|\");\n");
		phpLogCode.add("fwrite($file, $_SERVER[\"REMOTE_ADDR\"]);\n");
		phpLogCode.add("fwrite($file,\"|\");\n");
		phpLogCode.add("fwrite($file, $_SERVER['HTTP_USER_AGENT']);\n");
		phpLogCode.add("fwrite($file,'<br>\n');\n");
		phpLogCode.add("fclose( $file );\n");
		phpLogCode.add("?>\n");
		return phpLogCode;
	}
	
	
	public static void writeWeb(List<HashMap<Column, String>> data) {
		File file = new File("index.php");
		Date timestamp = new Date();
		List<String> phpLogCode = loadPhpLog(false);
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
			List<String> cssFiles = getWebFiles(WebFileType.CSS, false);
			for(String cssFile : cssFiles)
				buffer.write("<link rel=\"stylesheet\" href=\"css/"+ cssFile + "\" />\n");

			
			buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\" />");
			
			buffer.write("</head>\n");
			buffer.write("<body>\n");
			buffer.write("<div class=\"header\" id=\"myHeader\">\n");
			buffer.write("<ul>\n");
			
			/*
			 * This line allows the settings table to scroll horizontally. 
			 * It's a wonder that the main table doesn't need it and yet can
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
			String cleanTimestamp = formatTimestamp2(timestamp); 
			buffer.write("<input id=\"checkboxId\" type=\"checkbox\" onclick=\"resetValues()\" name=\"resetFilterValues\"/><div class=\"tooltip\">Reset Values<span class=\"tooltiptext\">Generated on " + cleanTimestamp + "</span></div>\n");
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
								tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\"><a href=\"financeChart.html?s=" + symbol + "\" target=\"_blank\">" + String.format("%.2f", Double.valueOf(data.get(row).get(column))) + "</td>";
							}else if(column == Column.OIDR || column == Column.MIDR || column == Column.MKTCAP){
								tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\">" + String.format("%.2f", Double.valueOf(data.get(row).get(column))) + "</td>";
							}else{
								tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\">" + String.format("%.1f", Double.valueOf(data.get(row).get(column))) + "</td>";
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
			buffer.write("\n");
			
			//First if of text
			buffer.write("if (te1 && te2 && te3 && te4) {\n");
			buffer.write("var exist = false;\n");
			for(int j=0;j<4;j++)
				buffer.write("var s" + (j+1) +" = te" + (j+1) + ".innerHTML.toUpperCase().replace(/<[^>]+>/g,\"\");\n");
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
					giantCondition += "Number(td" + i + ".innerHTML.replace(/%$/g,\"\").replace(/N.A/,'-1')) > Number(min" + i +") &&\n";
					giantCondition += "Number(td" + i + ".innerHTML.replace(/%$/g,\"\").replace(/N.A/,'-1')) < Number(max" + i +") &&\n";
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
			
			buffer.write("<script type=\"text/javascript\">\n");
			buffer.write("if (screen.width <= 699) {\n");
			buffer.write("document.location = \"mobile.php\"\n");
			buffer.write("}\n");
			buffer.write("</script>\n");
			
			List<String> jsFiles = getWebFiles(WebFileType.JS, false);
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
	
	@SuppressWarnings("deprecation")
	private static String formatTimestamp2(Date date){
		return ((date.getYear()+1900)%100) + "." + (date.getMonth()+1) + "." + date.getDate() + "." + date.getHours() + "." + date.getMinutes() + "." + date.getSeconds();
	}
	
	enum WebFileType {
		CSS,
		JS
	}
	
	private static List<String> getWebFiles(WebFileType wbf, boolean isMobile){
		String extension = wbf.toString().toLowerCase();
		File folder = 	new File(extension + "/");
		List<String> files = new ArrayList<String>();
		for(File file : folder.listFiles())
			if(file.isFile() && file.getName().endsWith(extension))
				if(isMobile && !file.getName().contains("Desktop"))
					files.add(file.getName().replaceAll(".*/", ""));
				else if(!isMobile && !file.getName().contains("Mobile")){
					files.add(file.getName().replaceAll(".*/", ""));
				}
		Collections.sort(files);
		return files;
	}
}