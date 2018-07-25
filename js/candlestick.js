function plot(stock){
Plotly.d3.csv('http://173.88.132.105/stockprophet/' + stock + '.json', function(err, rows){

function unpack(rows, key) {
  return rows.map(function(row) {
    return row[key];
  });
}

var price = {
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

var fit3 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit3'),
  line: {
  	color: '#0011FF',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};

var fit4 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit4'),
  line: {
  	color: '#2277DD',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};

var fit5 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit5'),
  line: {
  	color: '#44CCBB',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};

var fit6 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit6'),
  line: {
  	color: '#66FF99',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};

var fit7 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit7'),
  line: {
  	color: '#AAFF55',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};

var fit8 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit8'),
  line: {
  	color: '#CCCC33',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};

var fit9 = {
  x: unpack(rows, 'Date'),
  y: unpack(rows, 'Fit9'),
  line: {
  	color: '#FFAA33',
  	width: 1
  },
  mode: 'lines',
  type: 'scatter',
  xaxis: 'x',
  yaxis: 'y'
};


var data = [price, fit3, fit4, fit5, fit6, fit7, fit8, fit9];

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
