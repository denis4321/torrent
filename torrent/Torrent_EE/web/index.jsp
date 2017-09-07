<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
<link type="text/css" rel="stylesheet" href="scripts/css/style.css">
<script type="text/javascript" src="scripts/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="scripts/jquery.rotate.1-1.js"></script>
<script type="text/javascript" src="scripts/rotate.js"></script>
<script type="text/javascript" src="scripts/mouseOver.js"></script>
<script type="text/javascript" src="scripts/custom.js"></script>
<title>
    Torrent Mobile
</title>
<style>
    #downloads{
        margin-top: 700px;
        position: absolute;
        margin-left: 10%;
    }
    #downloads img{
        width: 120px;
        height: 120px;
        
    }
    .info{
    width: 190px;
    text-align: center;
    }
</style>
</head>
<body>
<script type="text/javascript" >

$(document).ready(function() {


$(".coffeeCup").hover(
      function () {
	$(this).attr('src','images/coffee-over.png');
      },
	function () {
	$(this).attr('src','images/coffee-out.png');
      }
    );




$(".iMac").hover(
      function () {
	$(this).attr('src','images/iMac-over.png');
      },
	function () {
	$(this).attr('src','images/iMac-out.png');
      }
    );


$(".AppleIpad").hover(
      function () {
	$(this).attr('src','images/iPad-over.png');
      },
	function () {
	$(this).attr('src','images/iPad-out.png');
      }
    );


$(".iPhone").hover(
      function () {
	$(this).attr('src','images/iPhone-over.png');
      },
	function () {
	$(this).attr('src','images/iPhone-out.png');
      }
    );


$(".magicMouse").hover(
      function () {
	$(this).attr('src','images/mouse-over.png');
      },
	function () {
	$(this).attr('src','images/mouse-out.png');
      }
    );

 });



</script>





<div id="coffee">
<img class="coffeeCup" alt="Coffee" src="images/coffee-out.png" >
</div>

<div class="headset"></div>


<a href=""><img class="charger" src="images/Blue.png" title="Help" alt="Coffee"></a>




<div id="iPhone">
<a href=""><img class="iPhone" src="images/iPhone-out.png" title="Download ME application"></a>
</div>
<div id="iMac">
<img alt="iMac" class="iMac" src="images/iMac-out.png">
</div>
<div id="keyboard">
<img src="images/keyboard.png">
</div>
<div id="iPad">
<a href=""><img class="AppleIpad" alt="iPad" src="images/iPad-out.png" title="Download Android application"></a>
</div>
<div class="logo"> </div>

<div id="mouse">
<a href=""><img class="magicMouse" alt="Mouse" src="images/mouse-out.png" title="Download desktop programm"></a>
</div>

<div id="downloads">
    <center>
        <h1>Скачать приложения</h1>
        <table >
            <tr>
                <td class="info">Android<br>(для смартфона)</td>
                <td class="info">Java2ME<br>(для мобильного телефона)</td>
                <td class="info">Java2SE<br> (для компьютера)</td>
                <td class="info">BitTorrent<br> (торрент клиент)</td>
            </tr>
            <tr>
                <td class="info" height="190">
                    <a href=""> <img src="images/android.jpeg" id="android"></a>
                </td>
                <td class="info" height="190">
                    <a href=""> <img src="images/me.gif" id="me"></a>
                </td>
                <td class="info" height="190">
                    <a href=""> <img src="images/se.gif" id="se"></a>
                </td>
                <td class="info" height="190">
                    <a href="http://www.bittorrent.com/intl/ru/downloads"><img src="images/torrent.png" id="torrent"></a>
                </td>
            </tr>
        </table>
    </center>
</div>
</body>
<script type="text/javascript">
    $("#torrent, #android, #se, #me").hover(
      function () {
	//$(this).css({'width':'180px','height':'180px'});
        $(this).animate({'width':'180px','height':'180px'},700);
        
      },
	function () {
	//$(this).css({'width':'120px','height':'120px'});
        $(this).animate({'width':'120px','height':'120px'},700);
      }
    );

   
</script>
</html>