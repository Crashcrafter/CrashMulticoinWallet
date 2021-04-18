function currencydelete(id) {
    let xhttp = new XMLHttpRequest()
    xhttp.onreadystatechange = function (){
        if(this.readyState === 4 && this.status === 200){
            if(this.responseText === "success"){
                window.location.href = "/admin/currencies"
            }else {
                window.location.href = "/admin/currencies?error=idnotfound"
            }
        }
    }
    xhttp.open("POST", "/admin/currencies?delete=" + id, true)
    xhttp.send()
}

function currencyaddnew() {
    window.location.href = "/admin/currencies?addnew"
}

function loadnewcurrencyform() {
    console.log("test")
    let typeobj = document.getElementById("assettype")
    let value = typeobj.options[typeobj.selectedIndex].value
    if(value === "unselected") return
    let xhttp = new XMLHttpRequest()
    xhttp.onreadystatechange = function (){
        if(this.readyState === 4 && this.status === 200){
            document.querySelector(".newcurrencyAjax").innerHTML = this.responseText
        }
    }
    xhttp.open("GET", "/admin/currencies?addnew=" + value, true)
    xhttp.send()
}