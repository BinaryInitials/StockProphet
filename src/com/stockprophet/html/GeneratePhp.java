package com.stockprophet.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GeneratePhp {
	
	
	public static final int RANK_INDEX = 0;
	public static final int DIFF_INDEX = 1;
	public static final int SYMBOL_INDEX = 2;
	public static final int COMPANY_INDEX = 3;
	
	
	public static final String[] DAYS = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	public static final String[] MONTHS = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	public static void write(List<HashMap<Column, String>> data) {
		File file = new File("index.php");
		Date timestamp = new Date();
		try{
			file.createNewFile();
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(writer);
			
			buffer.write("<?php\n");
			buffer.write("$file = fopen(\"stockprophet_log.html\", \"a\") or die(\"Unable to open file!\");\n");
			buffer.write("fwrite($file, date('Y-m-d|H:i:s'));\n");
			buffer.write("fwrite($file,\"|\");\n");
			buffer.write("fwrite($file, $_SERVER[\"REMOTE_ADDR\"]);\n");
			buffer.write("fwrite($file,\"|\");\n");
			buffer.write("fwrite($file, $_SERVER['HTTP_USER_AGENT']);\n");
			buffer.write("fwrite($file,'<br>');\n");
			buffer.write("fclose( $file );\n");
			buffer.write("?>\n");
			
			buffer.write("<!DOCTYPE html>\n");
			buffer.write("<html>\n");
			buffer.write("<head>\n");
			buffer.write("<title> STOCK PROPHET </title>\n");
			buffer.write("<link rel=\"stylesheet\" href=\"css/cssFile.css\" />\n");
			buffer.write("<link rel=\"stylesheet\" href=\"css/c12_2.css\" />\n");
			buffer.write("<link rel=\"stylesheet\" href=\"css/index.css\" />\n");
			buffer.write("<link rel=\"stylesheet\" href=\"css/prettify.css\"/>\n");
			buffer.write("<link rel=\"stylesheet\" href=\"css/ranking.css\"/>\n");
			buffer.write("</head>\n");
			buffer.write("<body>\n");
			buffer.write("<br>\n");
			buffer.write("<br>\n");
			
			buffer.write("<label2 id=\"header\">STOCK PROPHET</label2>\n");
			buffer.write("<font color=\"#333333\">" + formatTimestamp(timestamp) + "</font>\n");
			buffer.write("<br><font color=\"#AACCFF\">" + formatTimestamp2(timestamp) + "</font>\n");
			buffer.write("<ul>\n");
			
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
			
			buffer.write("<div>\n");
			buffer.write("<input id=\"checkboxId\" type=\"checkbox\" onclick=\"resetValues()\" name=\"resetFilterValues\"/>Reset Values\n");
			buffer.write("</div>");
			
			buffer.write("<br>\n");
			buffer.write("<br>\n");
			buffer.write("<div id=\"search\">\n");
			buffer.write("<input type=\"text\" size=\"2\" placeholder=\"Search...\" onkeyup=\"search()\" id=\"filter-search\" style=\"width: 500px;\"/>\n");
			buffer.write("</div>\n");
			
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
				
				int[] color = generateColorsFromColorMap(Double.valueOf(data.get(row).get(Column.SCORE))/100.0, colorMap);
				
				
				String tableRow = "<tr>";
				int columnIndex=0;
				String symbol = data.get(row).get(Column.SYMBOL);
				for(Column column : Column.values()){
					if(columnIndex == RANK_INDEX){
						tableRow += "<td  class=\"tbl-prevrank-icon\" style=\"background-color: #" + convertToColor(color) +  "\">" + (row+1) + "    <span class=\"rank-" + rankQualifier + "\" />" + "</td>";
					}else if(columnIndex == DIFF_INDEX){
						tableRow += "<td style=\"background-color: #" + convertToColor(color) + "\">" + sign + diff + "</td>";
					}else if(columnIndex == SYMBOL_INDEX){
						tableRow += "<td style=\"background-color: #" + convertToColor(color) + "\"><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" + symbol + "</td>";
					}else if(columnIndex == COMPANY_INDEX){
						tableRow += "<td style=\"background-color: #" + convertToColor(color) + "\"><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" +  data.get(row).get(Column.COMPANY) + "</td>";
					}else{
						if(data.get(row).get(column) != null){
							tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\">" + String.format("%.1f", Double.valueOf(data.get(row).get(column))) + "</td>";
						}else{
							tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, columnIndex)) + "\">null</td>";
						}
					}
					columnIndex++;
				}
				tableRow += "</tr>\n";
				buffer.write(tableRow);
			}
			
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
			
			
			
			
			buffer.write("function search() {\n");
			buffer.write("var input, filter, table, tr, td1, td2, i;\n");
			buffer.write("input = document.getElementById(\"filter-search\");\n");
			buffer.write("filter = input.value.toUpperCase();\n");
			buffer.write("table = document.getElementById(\"myTable\");\n");
			buffer.write("tr = table.getElementsByTagName(\"tr\");\n");
			buffer.write("for (i = 0; i < tr.length; i++) {\n");
			buffer.write("td1 = tr[i].getElementsByTagName(\"td\")[" + SYMBOL_INDEX + "];\n");
			buffer.write("td2 = tr[i].getElementsByTagName(\"td\")[" + COMPANY_INDEX + "];\n");
			buffer.write("if (td1 && td2) {\n");
			buffer.write("if (td1.innerHTML.toUpperCase().indexOf(filter) > -1 || td2.innerHTML.toUpperCase().indexOf(filter) > -1) {\n");
			buffer.write("tr[i].style.display = \"\";\n");
			buffer.write("} else {\n");
			buffer.write("tr[i].style.display = \"none\";\n");
			buffer.write("}\n");
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
			int columnIndex=0;
			i=1;
			for(Column column : Column.values()){
				if(column.isFilterable())
					buffer.write("td" + i++ + " = tr[i].getElementsByTagName(\"td\")[" + columnIndex + "];\n");
				columnIndex++;
			}
			buffer.write("\n");
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
			giantCondition += "true\n){\n";
			buffer.write(giantCondition);
			buffer.write("tr[i].style.display = \"\";\n");
			buffer.write("} else {\n");
			buffer.write("tr[i].style.display = \"none\";\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("</script>\n");
			
			buffer.write("<script src=\"js/jquery-1.11.0.js\"></script>\n");
			buffer.write("<script src=\"js/sort-table.js\"></script>\n");

			buffer.write("</tbody>\n");
			buffer.write("</table>\n");
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
	private static String formatTimestamp(Date date){
		return DAYS[(date.getDay())] + ", " + MONTHS[date.getMonth()] + " " + date.getDate() + ", " + (date.getYear() + 1900);
	}
	
	@SuppressWarnings("deprecation")
	private static String formatTimestamp2(Date date){
		return ((date.getYear()+1900)%100) + "." + (date.getMonth()+1) + "." + date.getDate() + "." + date.getHours() + "." + date.getMinutes() + "." + date.getSeconds();
	}
	
	private static HashMap<Integer, int[]> generateColorMap(){
		HashMap<Integer, int[]> colorMap = new HashMap<Integer, int[]>();
		colorMap.put(100, new int[]{250,250,250});	//WHITE
		colorMap.put(95, new int[]{150,200,250});	//CYAN
		colorMap.put(90, new int[]{0,50,250});	//BLUE
		colorMap.put(85, new int[]{0,250,50});	//GREEN
		colorMap.put(80, new int[]{150,250,50});	//CHARTREUSE
		colorMap.put(75, new int[]{250,250,50});	//YELLOW
		colorMap.put(70, new int[]{250,150,50});	//ORANGE
		colorMap.put(65, new int[]{250,25,25});	//RED
		colorMap.put(60, new int[]{150,15,15});	//BROWN
		colorMap.put(55, new int[]{50,0,0});	//DARK BROWN
		colorMap.put(50, new int[]{0,0,0});	//BLACK
		return colorMap;
	}
	
	private static String convertToColor(int[] rgb){
		String hexaColor = "";
		for(int color : rgb){
			String temp = Integer.toHexString(color).toUpperCase();
			hexaColor += temp.length() == 1 ? "0"+temp: temp;
		}
		return hexaColor;
	}
	
	private static int[] darken(int[] color, int level){
		int[] darker = new int[3];
		for(int i=0;i<darker.length;i++)
			darker[i] = darkenSingleChannel(color[i], level);
		return darker;
	}
	
	private static final int DARK_GRADIENT = 5; 
	private static int darkenSingleChannel(int color, int level){
		return color-DARK_GRADIENT*level > DARK_GRADIENT ? color-DARK_GRADIENT*level:DARK_GRADIENT;
	}
	
	private static int[] generateColorsFromColorMap(double metric, HashMap<Integer, int[]> colorMap){
		if(metric > 0.5){
			for(int key=95;key>=0;key-=5){
				if(key < metric * 100){
					int[] rgb1 = colorMap.get(key+5);
					int[] rgb2 = colorMap.get(key);
					int[] rgb = new int[3];
					for(int i=0;i<3;i++){
						rgb[i] = (int)Math.round((rgb1[i] - rgb2[i])*(100.0*metric - key)/5.0 + rgb2[i]); 
					}
					return rgb;
				}
			}
		}else if(metric > 0){
			return new int[]{0,0,0};
		}
		int gray = (int)Math.round(-metric*250);
		return new int[]{gray, gray, gray};
	}

}
