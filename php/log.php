<?php
$file = fopen("stockprophet_log.html", "a") or die("Unable to open file!");
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