
<?php
if(($_SERVER["REQUEST_METHOD"]=="GET") && (isset($_GET['place_id']))){

   $id=$_GET['place_id'];
    $encodedid = utf8_encode($id);

    $loc = file_get_contents("https://maps.googleapis.com/maps/api/place/details/json?placeid=$encodedid&key=AIzaSyC0NBHxide7tWiHP-MFJYpkHyOW_ShZ2sE");

    $myobj = json_decode($loc);

    if (property_exists($myobj->result, "photos")) {
      $pic = $myobj->result->photos;

      //echo count($pic);
      $res['photos']=array();

      for($i=0; $i<count($pic) || $i<5; $i++)
      {
          //echo $pic[$i]->photo_reference;

          $picId=$pic[$i]->photo_reference;

          if(!file_exists("images/$picId.jpg"))
          {
            file_put_contents("images/$picId.jpg", file_get_contents("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$picId&key=AIzaSyC0NBHxide7tWiHP-MFJYpkHyOW_ShZ2sE"));        
            chmod("images/$picId", 0777);
          }
          $res['photos'][]="images/$picId.jpg";
    }
  } else {
    $res['photosError'] = "No pictures to display!";
  }
if (property_exists($myobj->result, "reviews")) {
$res['reviews']=$myobj->result->reviews;
} else {
  $res['reviewsError'] = "No reviews to display!";
}
echo json_encode($res);
}
else{

?>
<html>
<head>
 <meta charset="utf-8">
  <style>
  * [hidden]{
    display:none !important;

  }
  .table {
  border: 1px solid silver ;
  background-color: #FAFAFA;
  margin-left: 370px;
  margin-right:370px;

}
p[onClick]:hover{
  cursor : pointer;

}
h1{
  font-family:serif;
  font-style: italic;
  font-weight: 200;
  padding-left:120px;
  margin-top:3px;
}
hr{
  margin-left: 10px;
  margin-right: 10px;
  margin-top:-20px;
  color:white;
}
form{
  margin-left:10px;
}
.cat{
  padding-top:5px;
}
.radi{

margin-left:305px;
margin-top:-20px;

}
.submit{
margin-left:70px;
}
summary{
  display: block;
}
summary>img{
  width:2em;

}
summary>p{
  line-height: 0;
  margin-bottom: 10px;
}
summary:hover{
  cursor: pointer;
}
  </style>
  <script async defer
     src="https://maps.googleapis.com/maps/api/js?key=AIzaSyANJaQqV1wYf9NNt-CzX3e6_Q-NxvAa25s">
     </script>

  <script type="application/javascript">
 function getIp() {
   var xmlhttp;

   if (window.XMLHttpRequest)
       {
           xmlhttp=new XMLHttpRequest();
       }
        xmlhttp.open("GET", "http://ip-api.com/json", false);
        xmlhttp.send();


        jsonObj= JSON.parse(xmlhttp.responseText);
        myjson=JSON.stringify(jsonObj);

        document.getElementById("ip").value=myjson;



 }


  </script>




</head>
<body>
  <div class="table">
    <h1> Travel and Entertainment Search </h1>
    <hr>

    <form id="myform" method="POST" action="">
      <b>
      Keyword&nbsp;<input type="text" name="kword"  required value=<?php if(isset($_POST['kword'])) echo $_POST['kword']; else echo "";?> >

      <br>
      <div class="cat">
      Category&nbsp;
  <select name="category" >
  <option selected="selected" name="default">default</option>
  <option value="cafe" name="Cafe">Cafe</option>
  <option value="bakery" name="bakery">bakery</option>
  <option value="rest" name="Restaurant">Restaurant</option>
  <option value="salon" name="Beauty Salon">Beauty Salon</option>
  <option value="casino" name="Casino">Casino</option>
  <option value="movie" name="Movie Theatre">Movie Theatre</option>
  <option value="lodge" name="lodging">lodging</option>
  <option value="airport" name="Airport">Airport</option>
  <option value="train" name="train Station">train Station</option>
  <option value="subway" name="subway Station">subway Station</option>
  <option value="bus" name="bus Station">bus Station</option>
</select>
</div>
<div class="cat">
Distance(miles)&nbsp;<input type="text" name="dist" placeholder="10"  value=<?php if(isset($_POST['dist'])) echo $_POST['dist']; else echo "";?> >
from
<div class="radi">
  <input onclick="getIp(); document.getElementById('here').disabled = false; document.getElementById('local1').disabled = true;" type="radio" name="location" value="here" id="here" checked="checked"> Here<br>
  <input onclick="document.getElementById('local1').disabled = false; document.getElementById('here').disabled = true;" type="radio" name="location" value="location" >
  <input id="local1" type="text" name="local" placeholder="location" required disabled="disabled" value=<?php if(isset($_POST['local'])) echo $_POST['local']; else echo "";?> ><br>
</div>
</div>
<input id="ip" value="" type="hidden" name="ip">
<div class="submit">
<input type="submit" value="Search" name="submit" onClick="getIp(); document.getElementById('myForm').submit();">
<input type="button" onclick="myFunction()" value="Clear" name="clear">
</div>
</b>
</form>
</div>
<div id="demo1">
</div>
<div id="map">
</div>

<?php

if(isset($_POST['submit'])){
$str_json = $_POST["ip"];

$response = json_decode($str_json, true);

  $lName = $response["org"];
  $age = $response["city"];

  $lat=$response["lat"];
  $lon=$response["lon"];


   $kword = $_POST["kword"];
   $dist=$_POST["dist"];
   $cat=$_POST["category"];

if($dist == "")
  $dist=10;

   $met= $dist * 1609.34;

   $encodedlat = utf8_encode($lat);
   $encodedlon = utf8_encode($lon);
   $encodedmet = utf8_encode($met);
   $encodedcat = utf8_encode($cat);
   $encodedkword = utf8_encode($kword);


//echo $met;


$loc = file_get_contents("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$encodedlat,$encodedlon&radius=$encodedmet&type=$encodedcat&keyword=$encodedkword&key=AIzaSyANJaQqV1wYf9NNt-CzX3e6_Q-NxvAa25s");
  $myobj = json_decode($loc);


?>
<script type="text/javascript">
  var stuff = <?php print json_encode($myobj);?>;

  //console.log(stuff);


stuff.onload=generateHTML(stuff);
    function generateHTML(jsonObj)
{

html_text="<br/>";
    html_text+="<center><div id='table'><table border='1' height='600' width='1200' >";

    html_text+="<tbody>";

    var res=jsonObj.results;

    var length=res.length;

  //  console.log(length);

    if(length>0){

html_text+="<tr>";

    html_text+="<th>"+"Category"+"</th>";
    html_text+="<th>"+"Name"+"</th>";
    html_text+="<th>"+"Address"+"</th>";
html_text+="</tr>";

var res_keys=Object.keys(res);



    //  console.log(res_keys);

  //   console.log(res[0]);

    for(i=0;i<res_keys.length;i++)
    {

    planeNodeList=res[i];


    console.log(planeNodeList);

    html_text+="<tr>";

    var row_keys = Object.keys(planeNodeList);

  //  console.log(row_keys.length);

    for(j=0;j<row_keys.length;j++)
    {
      prop = row_keys[j];

      if(row_keys[j]=="icon"){
        html_text+="<td><img src='"+ planeNodeList[prop]+ "' width='"+30+"' height='"+30+"'></td>";
      }
      else if(row_keys[j]=="name")
    {
         html_text+="<td><p onClick=viewReview('"+planeNodeList['place_id']+"')>"+planeNodeList[prop] +"</p></td>";
    }
    else if(row_keys[j]=="vicinity")
  {
       html_text+="<td><p onClick=viewMap("+planeNodeList['geometry']['location']['lat']+","+planeNodeList['geometry']['location']['lng']+")>"+planeNodeList[prop]+"</td>";
  }


    }


    html_text+="</tr>";
}



    html_text+="</tbody>";
    html_text+="</table>";
    html_text+="</div>"
    html_text+="</center>";

    document.getElementById("demo1").innerHTML=html_text;

  }
  else

  document.getElementById("demo1").innerHTML="No Records had been found!";

}

function viewReview(place_id){
  var xmlhttp;

  if (window.XMLHttpRequest)
      {
          xmlhttp=new XMLHttpRequest();
      }
       xmlhttp.open("GET", "index.php?place_id="+place_id, false);
       xmlhttp.send();


      jsonObj= JSON.parse(xmlhttp.responseText);

    //  console.log(jsonObj);


       document.getElementById("table").setAttribute("hidden",true);

       jsonObj.onload=genTable(jsonObj);



      function genTable(jsonObj)
       {


       html_text="<br/>";


           html_text+="<center><div id='table2'><details><summary><p>Click to show reviews</p><img src='http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png'/></summary><table border='1' height='400' width='500' >";

           html_text+="<tbody>";

           var res=jsonObj.reviews;

           //console.log(res);

           var res_keys=Object.keys(res);

           //console.log(res_keys);

           for(i=0;i<res_keys.length;i++)
           {

             planeNodeList=res[i];

       var res_keys=Object.keys(res);



            // console.log(res_keys);

           //  console.log(res[0]);

           for(i=0;i<res_keys.length;i++)
           {

           planeNodeList=res[i];

           var row_keys = Object.keys(planeNodeList);


           //console.log(planeNodeList);



           var row_keys = Object.keys(planeNodeList);

           //console.log(row_keys.length);


            html_text+="<td><center><img src='"+ planeNodeList['profile_photo_url']+ "' width='"+40+"' height='"+40+"'>"+planeNodeList['author_name']+ "</center></td>";

            html_text+="<tr>";
            html_text+="<td>"+planeNodeList['text']+"</td>";
            html_text+="<tr>";


           }


       }
       html_text+="</tbody>";
       html_text+="</table></details>";
       html_text+="</div>"
       html_text+="</center>";


       document.getElementById("demo1").innerHTML=html_text;



       html_text="<br/>";
           html_text+="<center><div id='table3'><details><summary><p>Click to show photos</p><img src='http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png'/></summary><table border='1' height='400' width='500' >";


           var res=jsonObj.photos;

          // console.log(res);

           var res_keys=Object.keys(res);


        //   console.log(res_keys);

          // console.log(res_keys['1']);



           if(res_keys.length>5)
        {

           for(i=0;i<5;i++)
           {
             planeNodeList=res[i];

             console.log(planeNodeList);

            html_text+="<tr>";
            html_text+="<td><a href='"+planeNodeList+"'><img src='"+planeNodeList+"' width='"+485+"' height='"+300+"'></a></td>";
            html_text+="<tr>";


           }
           if(res_keys.length<5)
           {
             for(i=0;i<res_keys.length;i++)
             {
              html_text+="<tr>";
              html_text+="<td><img src='"+planeNodeList+"' width='"+485+"' height='"+200+"'></td>";
              html_text+="<tr>";


             }

           }


       }

       html_text+="</table></details>";
       html_text+="</div>"
       html_text+="</center>";


       document.getElementById("demo1").innerHTML+=html_text;

       }



       }
function viewMap(latitude,long)
{
  console.log(latitude);
  console.log(long);

  var uluru = {lat: latitude, lng: long};
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 4,
          center: uluru
        });
        var marker = new google.maps.Marker({
          position: uluru,
          map: map
        });

      
}



</script>
<?php
}

?>

</body>
</html>
<?php } ?>
