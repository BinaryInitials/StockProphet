<?php
$file = fopen("plot-log.html", "a") or die("Unable to open file!");
fwrite($file, date('Y-m-d|H:i:s'));
fwrite($file,"|");
fwrite($file, $_SERVER["HTTP_REFERER"]);
fwrite($file,"|");
fwrite($file, $_SERVER["HTTP_HOST"]);
fwrite($file,"|");
fwrite($file, $_SERVER["REMOTE_ADDR"]);
fwrite($file,"|");
fwrite($file, $_SERVER['HTTP_USER_AGENT']);
fwrite($file,'<br>
');
fclose( $file );
?>
<head>

<link rel="shortcut icon" type="image/x-icon" href="images/logo.ico" /><script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
<script src="js/candlestick.js"></script>
</head>
<body>
<?php
$symbol = $_GET['s'];
chdir('/home/pi/StockProphet/');
$output = shell_exec('sudo ./get-data.sh ' . $symbol);
$output = shell_exec('sudo mv ' . $symbol . '.json /var/www/html/stockprophet/json/');
?>

<div id="graph"></div>
<script>
plot(location.search.substring(1).replace(/.*=/g,''));
</script>
</body>
