
window.onscroll = function() {myFunction()};

var headerNode = document.getElementById("header");
var sticky = header.offsetTop;

    
console.log("hello");

function myFunction() {
console.log("hello22");
    if (window.pageYOffset > sticky) {
	header.classList.add("sticky");
    } else {
	header.classList.remove("sticky");
    }
}
