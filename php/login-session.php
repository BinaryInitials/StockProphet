<?php
session_start();
if(isset($_SESSION['loggedin']) && $_SESSION['loggedin'] == true){

}else{
header("Location: https://binaryinitials.com/stockprophet/");
}
?>