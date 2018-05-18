package com.stockprophet.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GeneratePhp {
	
	
	public static final int RANK_INDEX = 0;
	public static final int DIFF_INDEX = 1;
	public static final int SYMBOL_INDEX = 2;
	public static final int COMPANY_INDEX = 3;
	public static void writeMobile(List<HashMap<Column, String>> data){
		File file = new File("mobile.php");
		try{
			file.createNewFile();
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(writer);
			
			buffer.write("<?php\n");
			buffer.write("$file = fopen(\"stockprophet_mobile_log.html\", \"a\") or die(\"Unable to open file!\");\n");
			buffer.write("fwrite($file, date('Y-m-d|H:i:s'));\n");
			buffer.write("fwrite($file,\"|\");\n");
			buffer.write("fwrite($file, $_SERVER[\"REMOTE_ADDR\"]);\n");
			buffer.write("fwrite($file,\"|\");\n");
			buffer.write("fwrite($file, $_SERVER['HTTP_USER_AGENT']);\n");
			buffer.write("fwrite($file,'\n<br>');\n");
			buffer.write("fclose( $file );\n");
			buffer.write("?>\n");
			
			buffer.write("<!DOCTYPE html>\n");
			buffer.write("<html>\n");
			buffer.write("<head>\n");
			buffer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /> ");
			buffer.write("<title> STOCK PROPHET </title>\n");
			List<String> cssFiles = getWebFiles(WebFileType.CSS, true);
			for(String cssFile : cssFiles)
				buffer.write("<link rel=\"stylesheet\" href=\"css/"+ cssFile + "\" />\n");

			buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\" />\n");
			buffer.write("<style>\n");
			buffer.write("body {\n");
			buffer.write("margin: 0;\n");
			buffer.write("font-family: Arial;\n");
			buffer.write("}\n");
			buffer.write(".top-container {\n");
			buffer.write("background-color: #f1f1f1;\n");
			buffer.write("background: linear-gradient(180deg, #666666, #999999);\n");
			buffer.write("}\n");
			buffer.write(".header {\n");
			buffer.write("background: #f1f1f1;\n");
			buffer.write("background: linear-gradient(180deg, #777777, #F1F1F1);\n");
			buffer.write("color: #f1f1f1;\n");
			buffer.write("}\n");
			buffer.write(".content {\n");
			buffer.write("}\n");
			buffer.write(".sticky {\n");
			buffer.write("position: fixed;\n");
			buffer.write("top: 0;\n");
			buffer.write("width: 100%;\n");
			buffer.write("}\n");
			buffer.write(".sticky + .content {\n");
			buffer.write("padding-top: 120px;\n");
			buffer.write("}\n");
			buffer.write("</style>\n");			
			
			
			buffer.write("</head>\n");
			buffer.write("<body>\n");
			buffer.write("<div class=\"header\" id=\"myHeader\">\n");
			buffer.write("<ul>\n");
			
			buffer.write("<div id=\"search\">\n");
			buffer.write("<input type=\"text\" size=\"2\" placeholder=\"Search...\" onkeyup=\"search()\" id=\"filter-search\" />\n");
			buffer.write("</div>\n");
			buffer.write("</ul>\n");
			buffer.write("</div>\n");
			
			buffer.write("<div class=\"content\">\n");
			buffer.write("table id=\"myTable\" class=\"sortable\"><thead><tr><th data-sort=\"number\" style=\"width:100px;\">RANK</th><th data-sort=\"name\">SYMB</th><th data-sort=\"name\">YEAR1</th></tr></thead>\n");

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
				double score = (data.size() - Integer.valueOf(data.get(row).get(Column.RANKING))-1)/(double)data.size();
				int[] color = generateColorsFromColorMap(score, colorMap);
				
				String tableRow = "<tr>";
				String symbol = data.get(row).get(Column.SYMB);
				tableRow += "<td  class=\"tbl-prevrank-icon\" style=\"background-color: #" + convertToColor(color) +  "\">" + (row+1) + "    <span class=\"rank-" + rankQualifier + "\"></span> " + sign + diff +" </td>";
				tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, 1)) + "\"><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" + symbol + "</td>";
				tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, 2)) + "\">" + String.format("%.1f", Double.valueOf(data.get(row).get(Column.YEAR1))) + "</td>";
				tableRow += "</tr>\n";
				buffer.write(tableRow);
			}
			
			//Javascript
			buffer.write("<script>\n");
			
			buffer.write("function search() {\n");
			buffer.write("var input, filter, table, tr, td1, i;\n");
			buffer.write("input = document.getElementById(\"filter-search\");\n");
			buffer.write("filter = input.value.toUpperCase().split(\" \");\n");
			buffer.write("table = document.getElementById(\"myTable\");\n");
			buffer.write("tr = table.getElementsByTagName(\"tr\");\n");
			buffer.write("for (i = 0; i < tr.length; i++) {\n");
			buffer.write("td1 = tr[i].getElementsByTagName(\"td\")[" + (SYMBOL_INDEX-1) + "];\n");
			buffer.write("if (td1) {\n");
			buffer.write("var exist = false;\n");
			buffer.write("for(j=0;j<filter.length;j++){\n");
			buffer.write("if (td1.innerHTML.toUpperCase().indexOf(filter[j]) > -1) {\n");
			buffer.write("exist = true;\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("if(exist){\n");
			buffer.write("tr[i].style.display = \"\";\n");
			buffer.write("} else {\n");
			buffer.write("tr[i].style.display = \"none\"\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("}\n");
			
			
			buffer.write("</script>\n");
			
			
			buffer.write("<script>\n");
			buffer.write("window.onscroll = function() {myFunction()};\n");
			buffer.write("var header = document.getElementById(\"myHeader\");\n");
			buffer.write("var sticky = header.offsetTop;\n");
			buffer.write("function myFunction() {\n");
			buffer.write("if (window.pageYOffset >= sticky) {\n");
			buffer.write("header.classList.add(\"sticky\");\n");
			buffer.write("} else {\n");
			buffer.write("header.classList.remove(\"sticky\");\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("</script>\n");

			
			List<String> jsFiles = getWebFiles(WebFileType.JS, true);
			Collections.sort(jsFiles);
			for(String jsFile : jsFiles)
				buffer.write("<script src=\"js/" + jsFile + "\"></script>\n");

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
	
	public static void writeWeb(List<HashMap<Column, String>> data) {
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
			buffer.write("fwrite($file,'\n<br>');\n");
			buffer.write("fclose( $file );\n");
			buffer.write("?>\n");
			
			buffer.write("<!DOCTYPE html>\n");
			buffer.write("<html>\n");
			buffer.write("<head>\n");
			buffer.write("<title> STOCK PROPHET </title>\n");
			List<String> cssFiles = getWebFiles(WebFileType.CSS, false);
			for(String cssFile : cssFiles)
				buffer.write("<link rel=\"stylesheet\" href=\"css/"+ cssFile + "\" />\n");

			
			buffer.write("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"images/logo.ico\" />");
			buffer.write("<style>\n");
			buffer.write(".tooltip {\n");
			buffer.write("position: relative;\n");
			buffer.write("display: inline-block;\n");
			buffer.write("border-bottom: 1px dotted black;\n");
			buffer.write("}\n");
			buffer.write(".tooltip .tooltiptext {\n");
			buffer.write("visibility: hidden;\n");
			buffer.write("width: 120px;\n");
			buffer.write("background-color: #555;\n");
			buffer.write("color: #fff;\n");
			buffer.write("text-align: center;\n");
			buffer.write("border-radius: 6px;\n");
			buffer.write("padding: 5px 0;\n");
			buffer.write("position: absolute;\n");
			buffer.write("z-index: 1;\n");
			buffer.write("bottom: 125%;\n");
			buffer.write("left: 50%;\n");
			buffer.write("margin-left: -60px;\n");
			buffer.write("opacity: 0;\n");
			buffer.write("transition: opacity 0.3s;\n");
			buffer.write("}\n");
			buffer.write(".tooltip .tooltiptext::after {\n");
			buffer.write("content: \"\";\n");
			buffer.write("position: absolute;\n");
			buffer.write("top: 100%;\n");
			buffer.write("left: 50%;\n");
			buffer.write("margin-left: -5px;\n");
			buffer.write("border-width: 5px;\n");
			buffer.write("border-style: solid;\n");
			buffer.write("border-color: #555 transparent transparent transparent;\n");
			buffer.write("}\n");
			buffer.write(".tooltip:hover .tooltiptext {\n");
			buffer.write("visibility: visible;\n");
			buffer.write("opacity: 1;\n");
			buffer.write("}\n");
			buffer.write("</style>\n");
			
			buffer.write("<style>\n");
			buffer.write("body {\n");
			buffer.write("margin: 0;\n");
			buffer.write("font-family: Arial;\n");
			buffer.write("}\n");
			buffer.write(".top-container {\n");
			buffer.write("background-color: #f1f1f1;\n");
			buffer.write("background: linear-gradient(180deg, #666666, #999999);\n");
			buffer.write("padding: 30px;\n");
			buffer.write("}\n");
			buffer.write(".header {\n");
			buffer.write("padding: 10px 16px;\n");
			buffer.write("background: #f1f1f1;\n");
			buffer.write("background: linear-gradient(180deg, #777777, #F1F1F1);\n");
			buffer.write("color: #f1f1f1;\n");
			buffer.write("}\n");
			buffer.write(".content {\n");
			buffer.write("}\n");
			buffer.write(".sticky {\n");
			buffer.write("position: fixed;\n");
			buffer.write("top: 0;\n");
			buffer.write("width: 100%;\n");
			buffer.write("}\n");
			buffer.write(".sticky + .content {\n");
			buffer.write("padding-top: 320px;\n");
			buffer.write("}\n");
			buffer.write("</style>\n");

			buffer.write("</head>\n");
			buffer.write("<body>\n");
			buffer.write("<div class=\"header\" id=\"myHeader\">\n");
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
				double score = (data.size() - Integer.valueOf(data.get(row).get(Column.RANKING))-1)/(double)data.size();
				int[] color = generateColorsFromColorMap(score, colorMap);
				
				String tableRow = "<tr>";
				int columnIndex=0;
				String symbol = data.get(row).get(Column.SYMB);
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
			buffer.write("filter = input.value.toUpperCase().split(\" \");\n");
			buffer.write("table = document.getElementById(\"myTable\");\n");
			buffer.write("tr = table.getElementsByTagName(\"tr\");\n");
			buffer.write("for (i = 0; i < tr.length; i++) {\n");
			buffer.write("td1 = tr[i].getElementsByTagName(\"td\")[" + SYMBOL_INDEX + "];\n");
			buffer.write("td2 = tr[i].getElementsByTagName(\"td\")[" + COMPANY_INDEX + "];\n");
			buffer.write("if (td1 && td2) {\n");
			buffer.write("var exist = false;\n");
			buffer.write("for(j=0;j<filter.length;j++){\n");
			buffer.write("if (td1.innerHTML.toUpperCase().indexOf(filter[j]) > -1 || td2.innerHTML.toUpperCase().indexOf(filter[j]) > -1) {\n");
			buffer.write("exist = true;\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("if(exist){\n");
			buffer.write("tr[i].style.display = \"\";\n");
			buffer.write("} else {\n");
			buffer.write("tr[i].style.display = \"none\"\n");
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
			
			buffer.write("<script type=\"text/javascript\">\n");
			buffer.write("if (screen.width <= 699) {\n");
			buffer.write("document.location = \"mobile.php\"\n");
			buffer.write("}\n");
			buffer.write("</script>\n");
			
			
			buffer.write("<script>\n");
			buffer.write("window.onscroll = function() {myFunction()};\n");
			buffer.write("var header = document.getElementById(\"myHeader\");\n");
			buffer.write("var sticky = header.offsetTop;\n");
			buffer.write("function myFunction() {\n");
			buffer.write("if (window.pageYOffset >= sticky) {\n");
			buffer.write("header.classList.add(\"sticky\");\n");
			buffer.write("} else {\n");
			buffer.write("header.classList.remove(\"sticky\");\n");
			buffer.write("}\n");
			buffer.write("}\n");
			buffer.write("</script>\n");

			
			List<String> jsFiles = getWebFiles(WebFileType.JS, false);
			Collections.sort(jsFiles);
			for(String jsFile : jsFiles)
				buffer.write("<script src=\"js/" + jsFile + "\"></script>\n");

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
	private static String formatTimestamp2(Date date){
		return ((date.getYear()+1900)%100) + "." + (date.getMonth()+1) + "." + date.getDate() + "." + date.getHours() + "." + date.getMinutes() + "." + date.getSeconds();
	}
	
	private static int COLOR_INCREMENT = 10;
	
	private static HashMap<Integer, int[]> generateColorMap(){
		HashMap<Integer, int[]> colorMap = new HashMap<Integer, int[]>();
		int count = 10; 
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,250,250});	//WHITE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{150,200,250});	//CYAN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{0,50,250});	//BLUE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{0,250,50});	//GREEN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{150,250,50});	//CHARTREUSE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,250,50});	//YELLOW
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,150,50});	//ORANGE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,25,25});	//RED
		colorMap.put(COLOR_INCREMENT*count--, new int[]{150,15,15});	//BROWN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{50,0,0});	//DARK BROWN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{20,0,0});	//BLACK
		colorMap.put(COLOR_INCREMENT*count--, new int[]{0,0,0});	//BLACK
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
		for(int key=90;key>=-COLOR_INCREMENT;key-=COLOR_INCREMENT){
			if(key < metric * 100){
				int[] rgb1 = colorMap.get(key+COLOR_INCREMENT);
				int[] rgb2 = colorMap.get(key);
				int[] rgb = new int[3];
				for(int i=0;i<3;i++)
					rgb[i] = (int)Math.round((rgb1[i] - rgb2[i])*(100.0*metric - key)/(double)COLOR_INCREMENT + rgb2[i]);
				return rgb;
			}
		}
		return new int[]{0,0,0};
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
		return files;
	}
}