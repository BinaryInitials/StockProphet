function plot(stock){
Plotly.d3.csv('http://173.88.132.105/stockprophet/' + stock + '.json', function(err, rows){

function unpack(rows, key) {
  return rows.map(function(row) {
    return row[key];
  });
}

var trace1 = {
  x: unpack(rows, 'Date'),
  close: unpack(rows, 'Close'),
  high: unpack(rows, 'High'),
  low: unpack(rows, 'Low'),
  open: unpack(rows, 'Open'),

  // cutomise colors
  increasing: {line: {color: 'green'}},
  decreasing: {line: {color: 'red'}},

  type: 'candlestick',
  xaxis: 'x',
  yaxis: 'y'
};

var trace2 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit9'),
  line: {
  	color: 'rgb(50, 100, 120)',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};

var data = [trace1, trace2];

var layout = {
  dragmode: 'zoom',
  showlegend: false,
  xaxis: {
    rangeslider: {
		 visible: false
	 }
  }
};

Plotly.plot('graph', data, layout);
});
}
