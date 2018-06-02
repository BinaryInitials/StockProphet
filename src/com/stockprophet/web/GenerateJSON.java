package com.stockprophet.web;

import static com.stockprophet.web.Methods.convertToColor;
import static com.stockprophet.web.Methods.darken;
import static com.stockprophet.web.Methods.generateColorMap;
import static com.stockprophet.web.Methods.generateColorsFromColorMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenerateJSON {
	
	//NOTE: When using JSON, content is uploaded before header. So had to include header. But when this is done, column sorting doesn't work.
		public static void generateJsonForWeb(List<HashMap<Column, String>> data){
			List<String> rows = new ArrayList<String>();
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
				tableRow += "</tr>";
				rows.add(tableRow);
			}
			
			System.out.println("[");
			
			System.out.println("\t{");
			String tableHeader = "<table id=\"myTable\" class=\"sortable\"><thead><tr>";
			for(Column column : Column.values())
				tableHeader += "<th data-sort=\"" + (column.isNumber()? "number" : "name") + "\">" + column.name().toUpperCase() + "</th>";

			tableHeader += "</tr></thead>";
			System.out.println("\t\t\"row\":" + "\"" + tableHeader.replaceAll("\"","\\\\\"") + "\"");
			System.out.println("\t},");

			System.out.println("\t{");
			System.out.println("\t\t\"row\": \"<tbody>\"");
			System.out.println("\t},");
			
			for(String row : rows){
				System.out.println("\t{");
				System.out.println("\t\t\"row\":" + "\"" + row.replaceAll("\"","\\\\\"") + "\"");
				System.out.println("\t},");
			}
			
			System.out.println("\t{");
			System.out.println("\t\t\"row\":\"</tbody>\"");
			System.out.println("\t},");
			System.out.println("\t{");
			System.out.println("\t\t\"row\":\"</table>\"");
			System.out.println("\t}");
			
			System.out.println("]");
			
		}
		
		public static void generateJsonForMobile(List<HashMap<Column, String>> data, Column column) throws IOException{
			List<String> rows = new ArrayList<String>();
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
				String symbol = data.get(row).get(Column.SYMB);
				tableRow += "<td  class=\"tbl-prevrank-icon\" style=\"background-color: #" + convertToColor(color) +  "\">" + (row+1) + "    <span class=\"rank-" + rankQualifier + "\"></span> " + sign + diff +" </td>";
				tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, 3)) + "\"><a href=\"http://finance.yahoo.com/quote/" + symbol + "\" target=\"_blank\">" + symbol + "</td>";
				tableRow += "<td style=\"background-color: #" + convertToColor(darken(color, 5)) + "\">" + String.format("%.1f", Double.valueOf(data.get(row).get(column))) + "</td>";
				tableRow += "</tr>";
				rows.add(tableRow);
			}
			
			File file = new File("stocks_mobile_" + column.toString() + ".json");
			file.createNewFile();
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(writer);

			buffer.write("[\n");
			buffer.write("\t{\n");
			
			String tableHeader = "<table id=\"myTable\" class=\"sortable\"><thead><tr>";
			tableHeader += "<th data-sort=\"number\" style=\"width:100px;\">"+Column.RANK+"</th>";
			tableHeader	+= "<th data-sort=\"name\">"+Column.SYMB+"</th>";
			tableHeader += "<th data-sort=\"number\">"+column+"</th>";
			tableHeader += "</tr></thead>";

			buffer.write("\t\t\"row\":" + "\"" + tableHeader.replaceAll("\"","\\\\\"") + "\"\n");
			buffer.write("\t},\n");
			buffer.write("\t{\n");
			buffer.write("\t\t\"row\": \"<tbody>\"\n");
			buffer.write("\t},\n");
			
			for(String row : rows){
				buffer.write("\t{\n");
				buffer.write("\t\t\"row\":" + "\"" + row.replaceAll("\"","\\\\\"") + "\"\n");
				buffer.write("\t},\n");
			}
			
			buffer.write("\t{\n");
			buffer.write("\t\t\"row\":\"</tbody>\"\n");
			buffer.write("\t},\n");
			buffer.write("\t{\n");
			buffer.write("\t\t\"row\":\"</table>\"\n");
			buffer.write("\t}\n");
			
			buffer.write("]\n");
			buffer.close();
		}

}