
var $ = jQuery.noConflict();
//jQuery code that moves the page when clicking a menu.
$(document).ready(function(){
$(".scrollto").click(function() {
$.scrollTo($($(this).attr("href")), {
duration: 1000,
easing: 'easeOutCubic'
});
window.location.hash = $(this).attr("href");
return false;
});
});
$(document).ready(function(){
////Function To Make The iMac Image Change
$(function() {
$("#iMac img").autoMouseOver();
});
$("#iMac img").autoMouseOver();
$("#iMac img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to make the iPad Image Change.
$(function() {
$("#iPad img").autoMouseOver();
});
$("#iPad img").autoMouseOver();
$("#iPad img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to change the iPhone Screen.
$(function() {
$("#iPhone img").autoMouseOver();
});
$("#iPhone img").autoMouseOver();
$("#iPhone img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to change the coffee cup.
$(function() {
$("#coffee img").autoMouseOver();
});
$("#coffee img").autoMouseOver();
$("#coffee img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to change the mouse
$(function() {
$("#mouse img").autoMouseOver();
});
$("#mouse img").autoMouseOver();
$("#mouse img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to change the home
$(function() {
$("#home img").autoMouseOver();
});
$("#home img").autoMouseOver();
$("#home img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to change the about
$(function() {
$("#about img").autoMouseOver();
});
$("#about img").autoMouseOver();
$("#about img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to change the portfolio
$(function() {
$("#portfolio img").autoMouseOver();
});
$("#portfolio img").autoMouseOver();
$("#portfolio img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to change the contact
$(function() {
$("#contact img").autoMouseOver();
});
$("#contact img").autoMouseOver();
$("#contact img").autoMouseOver({ outStr: "_ot", overStr: "_ov" });
//////Function to start iPhone music.
$(function(){
var iPhoneHover = $('#iPhone');
var iPhoneAudio = iPhoneHover.find('audio')[0];
iPhoneHover.hover(function(){
iPhoneAudio.play();
},
function(){
iPhoneAudio.stop();
});
});
//////Function to start Keyboard music.
$(function(){
var KeyboardHover = $('#keyboard');
var KeyboardAudio = KeyboardHover.find('audio')[0];
KeyboardHover.hover(function(){
KeyboardAudio.play();
},
function(){
KeyboardAudio.stop();
});
});
//////Function to start iPad music.
$(function(){
var iPadHover = $('#iPad');
var iPadAudio = iPadHover.find('audio')[0];
iPadHover.hover(function(){
iPadAudio.play();
},
function(){
iPadAudio.stop();
});
});
jQuery(function($){
$('.fade').mosaic();
$('.bar').mosaic({
animation : 'slide' //fade or slide
});
});
});
$(document).ready(function(){
$("#mouse").mouseover(function(){
$('.magicMouse').animate({'rotate': 40});
$('.magicMouse').animate({
marginTop: "-20px",
marginLeft: "-20px",
}, 1500 );
});
$("#mouse").mouseover(function(){
$('.magicMouse').animate({'rotate':0});
$('.magicMouse').animate({
marginTop: "0px",
marginLeft: "40px;",
},1500);
});
$("#iPad").mouseover(function(){
$('.AppleIpad').animate({'rotate':-40});
});
$("#iPad").mouseover(function(){
$('.AppleIpad').animate({'rotate':0});
});
$("#html5logo").mouseover(function(){
$('#html5logo').animate({
marginTop:"20px",
},1000);
});
$("#html5logo").mouseout(function(){
$('#html5logo').animate({
marginTop:"0px",
},1000);
});
$("#skillItems").mouseover(function(){
$('#skillItems').animate({
opacity: 0.7,
},1000);
});
$("#skillItems").mouseout(function(){
$('#skillItems').animate({
opacity: 1,
},1000);
});
$("#contactSubmitBtn").mouseover(function(){
$('#contactSubmitBtn').animate({
opacity: 0.5,
},1000);
});
$("#contactSubmitBtn").mouseout(function(){
$('#contactSubmitBtn').animate({
opacity: 1,
},1000);
});
});
$(document).ready(function() {
$('#fadeIn').appleEffect({
color: '#FFF',
zIndex: 99999,
timeout: 2000,
speed: 3000
});
});