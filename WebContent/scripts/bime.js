
function doMenu(item) {
// obj=document.getElementById(item);
// col=document.getElementById("x" + item);
// if (obj.style.display=="none") {
//  obj.style.display="block";
//  col.innerHTML="[-]";
// }
// else {
//  obj.style.display="none";
//  col.innerHTML="[+]";
// }
}

function deletechecked(message)
{
    var answer = confirm(message)
    if (answer){
        document.messages.submit();
        return false;
    }
    
    return false;  
}