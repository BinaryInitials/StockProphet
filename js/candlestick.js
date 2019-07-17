function plot(stock){
var rawDataURL = 'json/' + stock + '.json';
var xField = 'Date';
var oField = 'Open';
var hField = 'High';
var lField = 'Low';
var cField = 'Close';

var selectorOptions = {
    buttons: [
    {
        step: 'month',
        stepmode: 'backward',
        count: 1,
        label: '1m'
    }, {
        step: 'month',
        stepmode: 'backward',
        count: 3,
        label: '3m'
    }, {
        step: 'month',
        stepmode: 'backward',
        count: 6,
        label: '6m'
    }, {
        step: 'year',
        stepmode: 'todate',
        count: 1,
        label: 'YTD'
    }, {
        step: 'year',
        stepmode: 'backward',
        count: 1,
        label: '1y'
    }, {
        step: 'year',
        stepmode: 'backward',
        count: 2,
        label: '2y'
    }, {
        step: 'all',
    }],
};

Plotly.d3.csv(rawDataURL, function(err, rawData) {
    if(err) throw err;

    var data = prepData(rawData);
    var layout = {
        title: 'Time series for ' + stock,
        xaxis: {
            rangeselector: selectorOptions,
			rangeslider: {
				 visible: false
			}
        },
        showlegend: false
    };

    Plotly.plot('graph', data, layout);
});

function prepData(rawData) {
    var x = [];
    var h = [];
	var l = [];
	var o = [];
	var c = [];
	
    rawData.forEach(function(datum, i) {

        x.push(new Date(datum[xField]));
        h.push(datum[hField]);
        l.push(datum[lField]);
        o.push(datum[oField]);
        c.push(datum[cField]);
        
//         l2.push(datum[lowField]);
//         h2.push(datum[highField]);
//         mva5.push(datum[mva5Field]);
//         mva13.push(datum[mva13Field]);
    });
    
    
    var hband3 = [];
    var lband3 = [];
    var hband2 = [];
    var lband2 = [];
    var hband1 = [];
    var lband1 = [];
    var hband_5 = [];
    var lband_5 = [];
    
    var mva5 = [];
    var mva13 = [];
    
    var kernel = 5;
    var xmva = [];
    for(var i=0;i<c.length-kernel;i++){
      var sum = 0;
      var sum2 = 0;
      for(var j=0;j<kernel;j++){
        var entry = parseFloat(c[i+j]);
        sum+= entry;
        sum2+= entry*entry;
      }
      xmva.push(x[i+kernel])
      hband3.push(sum/kernel + 3*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))
      lband3.push(sum/kernel - 3*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))
      hband2.push(sum/kernel + 2*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))
      lband2.push(sum/kernel - 2*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))
      hband1.push(sum/kernel + 1*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))
      lband1.push(sum/kernel - 1*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))
      hband_5.push(sum/kernel + 0.5*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))
      lband_5.push(sum/kernel - 0.5*Math.sqrt(sum2/kernel - (sum/kernel)*(sum/kernel)))


    }
    
      var mvaST = [];
      var xmvaST = [];
      var kernelST = 5;
      for(var i=0;i<c.length-kernelST;i++){
        var sum = 0;
        for(var j=0;j<kernelST;j++){
          sum+= parseFloat(c[i+j]);
        }
        xmvaST.push(x[i+kernelST]);
        mvaST.push(sum/kernelST);
      }
      
      
      var xmvaLT = [];
      var mvaLT = [];
      var kernelLT = 13;
      for(var i=0;i<c.length-kernelLT;i++){
        var sum = 0;
        for(var j=0;j<kernelLT;j++){
          sum+= parseFloat(c[i+j]);
        }
        xmvaLT.push(x[i+kernelLT]);
        mvaLT.push(sum/kernelLT);
      }

    return [
        {hoverinfo: 'none', mode: 'lines', x: xmvaST, y: mvaST, line: {width: '1'}, marker: {color: 'rgb(0,200,255)', size: 0}},
        {hoverinfo: 'none', mode: 'lines', x: xmvaLT, y: mvaLT, line: {width: '1'}, marker: {color: 'rgb(0,0,255)', size: 0}},
        {hoverinfo: 'none', type: 'candlestick', x: x, close: c, high: h, low: l, open: o, increasing: {line: {color: '#00CC00'}}, decreasing: {line: {color: '#CC0000'}}},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: hband3, line: {width: 0}},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: lband3, line: {width: 0}, fill: 'tonexty', fillcolor: '#77777722'},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: hband2, line: {width: 0}},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: lband2, line: {width: 0}, fill: 'tonexty', fillcolor: '#77777722'},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: hband1, line: {width: 0}},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: lband1, line: {width: 0}, fill: 'tonexty', fillcolor: '#77777722'},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: hband_5, line: {width: 0}},
        {hoverinfo: 'none', mode: 'lines', x: xmva, y: lband_5, line: {width: 0}, fill: 'tonexty', fillcolor: '#77777722'},


    ];
}
}
